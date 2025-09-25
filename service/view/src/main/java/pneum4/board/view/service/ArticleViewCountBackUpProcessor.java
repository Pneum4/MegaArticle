package pneum4.board.view.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pneum4.board.view.entity.ArticleViewCount;
import pneum4.board.view.repository.ArticleViewCountBackUpRepository;

@Component
@RequiredArgsConstructor
public class ArticleViewCountBackUpProcessor {
    private final ArticleViewCountBackUpRepository articleViewCountBackUpRepository;

    @Transactional
    public void backUp(Long articleId, Long viewCount) {
        int result = articleViewCountBackUpRepository.updateViewCount(articleId, viewCount);
        if(result == 0) { // record가 없거나 조건에 맞지 않아서 update가 되지 않은 경우
            articleViewCountBackUpRepository.findById(articleId) // record가 없는 경우에만 record 추가
                    .ifPresentOrElse(success->{},
                            ()-> articleViewCountBackUpRepository.save(ArticleViewCount.init(articleId, viewCount))
                    );
        }
    }
}
