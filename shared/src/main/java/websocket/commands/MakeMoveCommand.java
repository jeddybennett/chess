package websocket.commands;

import chess.ChessMove;
import model.UserData;

import java.util.Objects;

public class MakeMoveCommand extends UserGameCommand{

    private final ChessMove move;

    public MakeMoveCommand(String authToken, int gameID, ChessMove chessMove) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = chessMove;
    }

    public ChessMove getMove(){
        return move;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        MakeMoveCommand that = (MakeMoveCommand) o;
        return Objects.equals(move, that.move);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), move);
    }
}
