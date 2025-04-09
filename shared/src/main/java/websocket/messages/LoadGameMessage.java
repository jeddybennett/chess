package websocket.messages;

import java.util.Objects;

public class LoadGameMessage<T> extends ServerMessage{
    private final T game;
    public LoadGameMessage(T game) {
        super(ServerMessageType.LOAD_GAME);
            this.game = game;
    }

    public T getGame(){
        return game;

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        LoadGameMessage<?> that = (LoadGameMessage<?>) o;
        return Objects.equals(game, that.game);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), game);
    }
}
