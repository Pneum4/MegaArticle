package pneum4.board.common.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pneum4.board.common.event.payload.*;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum EventType {
    ARTICLE_CREATED(ArticleCreatedEventPayload.class, Topic.PNEUM4_BOARD_ARTICLE),
    ARTICLE_UPDATED(ArticleUpdatedEventPayload.class, Topic.PNEUM4_BOARD_ARTICLE),
    ARTICLE_DELETED(ArticleDeletedEventPayload.class, Topic.PNEUM4_BOARD_ARTICLE),
    COMMENT_CREATED(CommentCreatedEventPayload.class, Topic.PNEUM4_BOARD_COMMENT),
    COMMENT_DELETED(CommentDeletedEventPayload.class, Topic.PNEUM4_BOARD_COMMENT),
    ARTICLE_LIKED(ArticleLikedEventPayload.class, Topic.PNEUM4_BOARD_LIKE),
    ARTICLE_UNLIKED(ArticleUnlikedEventPayload.class, Topic.PNEUM4_BOARD_LIKE),
    ARTICLE_VIEWED(ArticleViewedEventPayload.class, Topic.PNEUM4_BOARD_VIEW);

    private final Class<? extends EventPayload> payloadClass;
    private final String topic;

    public static EventType from(String type) {
        try {
            return valueOf(type);
        } catch (Exception e) {
            log.error("[EventType.from] type={}", type, e);
            return null;
        }
    }

    public static class Topic {
        public static final String PNEUM4_BOARD_ARTICLE = "pneum4-board-article";
        public static final String PNEUM4_BOARD_COMMENT = "pneum4-board-comment";
        public static final String PNEUM4_BOARD_LIKE = "pneum4-board-like";
        public static final String PNEUM4_BOARD_VIEW = "pneum4-board-view";
    }
}
