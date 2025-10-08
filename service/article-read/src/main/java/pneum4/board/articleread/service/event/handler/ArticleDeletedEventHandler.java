package pneum4.board.articleread.service.event.handler;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pneum4.board.articleread.repository.ArticleIdListRepository;
import pneum4.board.articleread.repository.ArticleQueryModelRepository;
import pneum4.board.articleread.repository.BoardArticleCountRepository;
import pneum4.board.common.event.Event;
import pneum4.board.common.event.EventType;
import pneum4.board.common.event.payload.ArticleDeletedEventPayload;

@Component
@RequiredArgsConstructor
public class ArticleDeletedEventHandler implements EventHandler<ArticleDeletedEventPayload> {
    private final ArticleIdListRepository articleIdListRepository;
    private final ArticleQueryModelRepository articleQueryModelRepository;
    private final BoardArticleCountRepository boardArticleCountRepository;

    @Override
    public void handle(Event<ArticleDeletedEventPayload> event) {
        ArticleDeletedEventPayload payload = event.getPayload();
        articleIdListRepository.delete(payload.getBoardId(), payload.getArticleId());
        // 목록에는 있는데 게시글 조회가 안되는 상황을 방지하기위해서, 목록에서 먼저삭제하고 게시글을 삭제한다.
        articleQueryModelRepository.delete(payload.getArticleId());
        boardArticleCountRepository.createOrUpdate(payload.getBoardId(), payload.getBoardArticleCount());
    }

    @Override
    public boolean supports(Event<ArticleDeletedEventPayload> event) {
        return EventType.ARTICLE_DELETED == event.getType();
    }
}
