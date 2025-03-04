package chess.movecalculators;
import chess.*;
import java.util.Collection;
import java.util.ArrayList;
public class RookMovesCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {

        int[] rowMoves = {1, -1, 0, 0};
        int[] colMoves = {0, 0, 1, -1};

        return PieceMovesCalculator.rookAndBishopMoves(board, position, rowMoves, colMoves);
    }
}