package pneum4.board.hotarticle.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pneum4.board.common.event.Event;
import pneum4.board.common.event.EventPayload;
import pneum4.board.hotarticle.repository.ArticleCreatedTimeRepository;
import pneum4.board.hotarticle.repository.HotArticleListRepository;
import pneum4.board.hotarticle.service.eventhandler.EventHandler;

import java.time.Duration;
// date & dateTime 구분
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class HotArticleScoreUpdater {
    private final HotArticleListRepository hotArticleListRepository;
    private final HotArticleScoreCalculator hotArticleScoreCalculator;
    private final ArticleCreatedTimeRepository articleCreatedTimeRepository;

    private static final long HOT_ARTICLE_COUNT = 10;
    private static final Duration HOT_ARTICLE_TTL = Duration.ofDays(10);

    public void update(Event<EventPayload> event, EventHandler<EventPayload> eventHandler) {
            Long articleId = eventHandler.findArticleId(event);
            LocalDateTime createdTime = articleCreatedTimeRepository.read(articleId);

            if(!isArticleCreatedToday(createdTime)) {
                return;
            }

            // like view comment 업데이트
            eventHandler.handle(event);
            // 업데이트 이후 다시 스코어 계산
            long score = hotArticleScoreCalculator.calculate(articleId);

            hotArticleListRepository.add(
                articleId,
                createdTime,
                score,
                HOT_ARTICLE_COUNT,
                HOT_ARTICLE_TTL
        );
    }
    private boolean isArticleCreatedToday(LocalDateTime createdTime) {
        return createdTime != null && createdTime.toLocalDate().equals(LocalDate.now());
    }
}
