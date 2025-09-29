package pneum4.board.common.outboxmessagerelay;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageRelay {
    // Outbox 테이블을 관리하는 JPA Repository
    private final OutboxRepository outboxRepository;
    // 여러 인스턴스 환경에서 shard(파티션)를 분배해주는 coordinator
    private final MessageRelayCoordinator messageRelayCoordinator;
    // Kafka로 메시지를 발행하는 템플릿
    private final KafkaTemplate<String, String> messageRelayKafkaTemplate;

    /**
     * 트랜잭션이 커밋되기 직전에 실행됨 (BEFORE_COMMIT).
     * OutboxEvent가 발행되면, Outbox 엔티티를 DB에 먼저 저장.
     * (Kafka 전송 전에 DB에 안전하게 기록하기 위한 단계)
     */
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void createOutbox(OutboxEvent outboxEvent) {
        log.info("[MessageRelay.createOutbox] outboxEvent={}", outboxEvent);
        outboxRepository.save(outboxEvent.getOutbox());
    }

    /**
     * 트랜잭션이 정상적으로 커밋된 직후(AFTER_COMMIT) 실행.
     * 비동기 스레드 풀(messageRelayPublishEventExecutor)에서 수행.
     * DB에 기록된 Outbox 데이터를 Kafka로 발행 시도.
     */
    @Async("messageRelayPublishEventExecutor")
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void publishEvent(OutboxEvent outboxEvent) {
        publishEvent(outboxEvent.getOutbox());
    }

    /**
     * Outbox 엔티티를 실제 Kafka로 발행하는 로직.
     * - Kafka에 이벤트를 전송
     * - 전송 성공 시 Outbox 테이블에서 삭제
     * - 실패하면 로그만 찍고 Outbox는 남겨둠 (다음 스케줄러에서 재시도)
     */
    private void publishEvent(Outbox outbox) {
        try {
            messageRelayKafkaTemplate.send(
                    outbox.getEventType().getTopic(),      // 이벤트 타입에 지정된 토픽
                    String.valueOf(outbox.getShardKey()),  // 파티셔닝 키
                    outbox.getPayload()                    // 전송할 실제 데이터(payload)
            ).get(1, TimeUnit.SECONDS); // Future 결과를 최대 1초 기다림 → 전송 성공/실패 확인
            outboxRepository.delete(outbox ); // 성공 시 Outbox에서 삭제
        } catch (Exception e) {
            log.error("[MessageRelay.publishEvent] outbox={}", outbox, e);
        }
    }

    /**
     * 주기적으로 실행되는 스케줄러.
     * - 실행 지연: 5초 후 첫 실행
     * - 이후 10초마다 반복 실행
     * - scheduler = "messageRelayPublishPendingEventExecutor" (스레드 풀 이름)
     *
     * 역할:
     * - shard를 coordinator를 통해 배정받음
     * - 각 shard에서 아직 처리되지 않은 Outbox 이벤트를 조회 (<= 현재시간-10초, 최대 100개)
     * - 남아있는 이벤트를 Kafka로 재발행 시도
     */
    @Scheduled(
            fixedDelay = 10,
            initialDelay = 5,
            timeUnit = TimeUnit.SECONDS,
            scheduler = "messageRelayPublishPendingEventExecutor"
    )
    public void publishPendingEvent() {
        AssignedShard assignedShard = messageRelayCoordinator.assignShards();
        log.info("[MessageRelay.publishPendingEvent] assignedShard size={}", assignedShard.getShards().size());

        for (Long shard : assignedShard.getShards()) {
            // 해당 shard에 속한, 10초 이상 지난 Outbox 이벤트를 최대 100개 가져옴
            List<Outbox> outboxes = outboxRepository.findAllByShardKeyAndCreatedAtLessThanEqualOrderByCreatedAtAsc(
                    shard,
                    LocalDateTime.now().minusSeconds(10),
                    Pageable.ofSize(100)
            );
            // 각각 Kafka 발행 시도
            for (Outbox outbox : outboxes) {
                publishEvent(outbox);
            }
        }
    }
}