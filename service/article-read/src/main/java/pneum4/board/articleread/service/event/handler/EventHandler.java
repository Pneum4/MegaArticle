package pneum4.board.articleread.service.event.handler;

import pneum4.board.common.event.Event;
import pneum4.board.common.event.EventPayload;

public interface EventHandler<T extends EventPayload> {
    void handle(Event<T> event);
    boolean supports(Event<T> event);
}
