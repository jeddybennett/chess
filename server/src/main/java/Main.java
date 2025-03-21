import chess.*;
import dataaccess.DataAccessException;
import exception.ResponseException;
import server.*;
public class Main {
    public static void main(String[] args) throws ResponseException, DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        Server server = new Server();
        server.run(8080);
        System.out.println("♕ 240 Chess Server: " + piece);

    }
}