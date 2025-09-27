package pneum4.board.hotarticle.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pneum4.board.hotarticle.repository.ArticleCommentCountRepository;
import pneum4.board.hotarticle.repository.ArticleLikeCountRepository;
import pneum4.board.hotarticle.repository.ArticleViewCountRepository;

import java.util.random.RandomGenerator;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class HotArticleScoreCalculatorTest {
    @InjectMocks // 테스트 진행할 클래스
    HotArticleScoreCalculator hotArticleScoreCalculator;

    @Mock // 테스트할 클래스에서 사용하는 가짜객체를 만듬
    ArticleLikeCountRepository articleLikeCountRepository;
    @Mock
    ArticleViewCountRepository articleViewCountRepository;
    @Mock
    ArticleCommentCountRepository articleCommentCountRepository;

    @Test
    void calculateTest() {
        Long articleId = 1L;
        long likeCount = RandomGenerator.getDefault().nextLong(100);
        long commentCount = RandomGenerator.getDefault().nextLong(100);
        long viewCount = RandomGenerator.getDefault().nextLong(100);

        // mock 객체에서 메서드를 불렀을때 반환할 값 지정
        given(articleLikeCountRepository.read(articleId)).willReturn(likeCount);
        given(articleViewCountRepository.read(articleId)).willReturn(viewCount);
        given(articleCommentCountRepository.read(articleId)).willReturn(commentCount);

        // when
        long score = hotArticleScoreCalculator.calculate(articleId);

        // then
        assertThat(score).isEqualTo(3*likeCount + 2*commentCount + 1*viewCount);
    }
}