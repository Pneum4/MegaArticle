package pneum4.board.articleread.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import pneum4.board.articleread.service.ArticleReadService;
import pneum4.board.common.event.Event;
import pneum4.board.common.event.EventPayload;
import pneum4.board.common.event.EventType;

@Slf4j
@Component
@RequiredArgsConstructor
public class ArticleReadEventConsumer {
    private final ArticleReadService articleReadService;

    @KafkaListener(topics = {
            EventType.Topic.PNEUM4_BOARD_ARTICLE,
            EventType.Topic.PNEUM4_BOARD_COMMENT,
            EventType.Topic.PNEUM4_BOARD_LIKE
    })
    public void listen(String message, Acknowledgment ack) {
        log.info("[ArticleReadEventConsumer.listen] message={}", message);
        Event<EventPayload> event = Event.fromJson(message);
        if (event != null) {
            articleReadService.handleEvent(event);
        }
        // ack에는 어디까지 처리했는지 offset이 있다
        // ack를 호출해야 해당 offset까지 커밋했다고 kafka에 알려준다 (수동커밋)
        ack.acknowledge();
    }
}