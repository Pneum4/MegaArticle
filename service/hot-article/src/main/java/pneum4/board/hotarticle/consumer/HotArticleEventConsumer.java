package pneum4.board.hotarticle.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import pneum4.board.common.event.Event;
import pneum4.board.common.event.EventPayload;
import pneum4.board.common.event.EventType;
import pneum4.board.hotarticle.service.HotArticleService;

@Slf4j
@Component
@RequiredArgsConstructor
public class HotArticleEventConsumer {
    private final HotArticleService hotArticleService;

    @KafkaListener(topics = {
            EventType.Topic.PNEUM4_BOARD_ARTICLE,
            EventType.Topic.PNEUM4_BOARD_COMMENT,
            EventType.Topic.PNEUM4_BOARD_LIKE,
            EventType.Topic.PNEUM4_BOARD_VIEW
    })
    // event json 객체와 ack를 받아서 -> event 처리하고 ack를 호출
    public void listen(String message, Acknowledgment ack) {
        log.info("[HotArticleEventConsumer.listen] received message={}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            hotArticleService.handleEvent(event);
        }
        ack.acknowledge();
    }
}
