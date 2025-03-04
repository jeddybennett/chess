package chess.movecalculators;
import chess.*;
import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int[] rowMoves = {1, 1, -1, -1};
        int[] colMoves = {1, -1, 1, -1};

       return PieceMovesCalculator.rookAndBishopMoves(board, position, rowMoves, colMoves);
    }
}

