package pneum4.board.hotarticle.service.eventhandler;

import pneum4.board.common.event.Event;
import pneum4.board.common.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
    Long findArticleId(Event<T> event);
}
