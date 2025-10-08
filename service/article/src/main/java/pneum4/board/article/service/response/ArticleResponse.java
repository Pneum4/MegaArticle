package pneum4.board.article.service.response;

import lombok.Getter;
import lombok.ToString;
import pneum4.board.article.entity.Article;

import java.time.LocalDateTime;

@Getter
@ToString
public class ArticleResponse {
    private Long articleId; // Long타입의 경우 큰값이 들어오면 유실가능 (string으로 응답하는게 안전)
    private String title;
    private String content;
    private Long boardId;
    private Long writerId;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static ArticleResponse from(Article article) {
        ArticleResponse response = new ArticleResponse();
        response.articleId = article.getArticleId();
        response.title = article.getTitle();
        response.content = article.getContent();
        response.boardId = article.getBoardId();
        response.writerId = article.getWriterId();
        response.createdAt = article.getCreatedAt();
        response.modifiedAt = article.getModifiedAt();
        return response;
    }
}
