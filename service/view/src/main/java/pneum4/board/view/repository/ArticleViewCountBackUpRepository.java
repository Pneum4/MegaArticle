package pneum4.board.view.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pneum4.board.view.entity.ArticleViewCount;

@Repository
public interface ArticleViewCountBackUpRepository extends JpaRepository<ArticleViewCount, Long> {
    @Query(
            value = """
                    update article_view_count set view_count=:viewCount
                    where article_id=:articleId and view_count<:viewCount
                    """,
            // view_count<:viewCount는 업데이트 순서가 섞일때를 대비한 방어코드임
            // 조회수 증가가 동시에 접근되면 100 300 200 순으로 들어올 수 있는데, 300으로 update해야됨
            nativeQuery = true
    )
    @Modifying
    int updateViewCount( // 만약 update가 이루어 지지 않으면 0반환(record가 없거나 조건이 틀린 경우), 이루어지면 1반환
            @Param("articleId") Long articleId,
            @Param("viewCount") Long viewCount
    );
}
