package chess;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor team_color);
}
