package pneum4.board.hotarticle.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import pneum4.board.common.event.Event;
import pneum4.board.hotarticle.repository.ArticleCreatedTimeRepository;
import pneum4.board.hotarticle.repository.HotArticleListRepository;
import pneum4.board.hotarticle.service.eventhandler.EventHandler;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreUpdaterTest {
    @InjectMocks
    HotArticleScoreUpdater hotArticleScoreUpdater;

    // 테스트할 클래스 맴버변수 가짜 생성
    @Mock
    HotArticleListRepository hotArticleListRepository;
    @Mock
    HotArticleScoreCalculator hotArticleScoreCalculator;
    @Mock
    ArticleCreatedTimeRepository articleCreatedTimeRepository;

    @Test
    void updateIfArticleNotCreatedTodayTest() {
        // given
        Long articleId = 1L;
        // 테스트할 클래스 맴버변수가 아닌 파마리터로 넘기는 값들을 가짜로 생성
        Event event = Mockito.mock(Event.class);
        EventHandler eventHandler = Mockito.mock(EventHandler.class);

        BDDMockito.given(eventHandler.findArticleId(event)).willReturn(articleId);

        LocalDateTime createdTime = LocalDateTime.now().minusDays(1);
        BDDMockito.given(articleCreatedTimeRepository.read(articleId)).willReturn(createdTime);

        // when
        hotArticleScoreUpdater.update(event, eventHandler);

        // then
        // eventhandler의 handled이 호출되지 않았는지 확인 + event인자로 handle함수를 호출했는지
        Mockito.verify(eventHandler, Mockito.never()).handle(event);
        // repository의 add가 호출되지 않았는지 확인 + 어떤 인자던지 상관없이 호출했는지 확인
        // any + Type으로 타입 체킹만 하고, 실제 값은 확인 안함
        Mockito.verify(hotArticleListRepository, Mockito.never())
                .add(anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class));
    }

    @Test
    void updateTime() {
        // given
        Long articleId = 1L;
        // 테스트할 클래스 맴버변수가 아닌 파마리터로 넘기는 값들을 가짜로 생성
        Event event = Mockito.mock(Event.class);
        EventHandler eventHandler = Mockito.mock(EventHandler.class);

        BDDMockito.given(eventHandler.findArticleId(event)).willReturn(articleId);

        LocalDateTime createdTime = LocalDateTime.now();
        BDDMockito.given(articleCreatedTimeRepository.read(articleId)).willReturn(createdTime);

        // when
        hotArticleScoreUpdater.update(event, eventHandler);

        // then
        // eventhandler의 handled이 호출되지 않았는지 확인 + event인자로 handle함수를 호출했는지
        Mockito.verify(eventHandler).handle(event);
        // repository의 add가 호출되지 않았는지 확인 + 어떤 인자던지 상관없이 호출했는지 확인
        // any + Type으로 타입 체킹만 하고, 실제 값은 확인 안함
        Mockito.verify(hotArticleListRepository)
                .add(anyLong(), any(LocalDateTime.class), anyLong(), anyLong(), any(Duration.class));
    }
}