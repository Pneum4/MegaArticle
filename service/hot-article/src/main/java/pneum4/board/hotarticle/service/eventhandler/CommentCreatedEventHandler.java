package pneum4.board.hotarticle.service.eventhandler;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pneum4.board.common.event.Event;
import pneum4.board.common.event.EventType;
import pneum4.board.common.event.payload.CommentCreatedEventPayload;
import pneum4.board.hotarticle.repository.ArticleCommentCountRepository;
import pneum4.board.hotarticle.utils.TimeCalculatorUtils;

@Component
@RequiredArgsConstructor
public class CommentCreatedEventHandler implements EventHandler<CommentCreatedEventPayload> {
    private final ArticleCommentCountRepository articleCommentCountRepository;

    @Override
    public void handle(Event<CommentCreatedEventPayload> event) {
        CommentCreatedEventPayload payload = event.getPayload();
        articleCommentCountRepository.createOrUpdate(
                payload.getArticleId(),
                payload.getArticleCommentCount(),
                TimeCalculatorUtils.calculateDurationToMidnight()
        );
    }

    @Override
    public boolean supports(Event<CommentCreatedEventPayload> event) {
        return EventType.COMMENT_CREATED == event.getType();
    }

    @Override
    public Long findArticleId(Event<CommentCreatedEventPayload> event) {
        return event.getPayload().getArticleId();
    }
}
