package pneum4.board.view.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pneum4.board.view.entity.ArticleViewCount;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ArticleViewCountBackUpRepositoryTest {
    @Autowired
    ArticleViewCountBackUpRepository articleViewCountBackUpRepository;
    @PersistenceContext
    EntityManager entityManager;

    @Test
    @Transactional
    void updateViewCountTest() {
        articleViewCountBackUpRepository.save(
                ArticleViewCount.init(1L, 0L)
        );
        entityManager.flush();
        entityManager.clear();

        int result1 = articleViewCountBackUpRepository.updateViewCount(1L, 100L);
        int result2 = articleViewCountBackUpRepository.updateViewCount(1L, 300L);
        int result3 = articleViewCountBackUpRepository.updateViewCount(1L, 200L);

        assertThat(result1).isEqualTo(1);
        assertThat(result2).isEqualTo(1);
        assertThat(result3).isEqualTo(0);
    }
}