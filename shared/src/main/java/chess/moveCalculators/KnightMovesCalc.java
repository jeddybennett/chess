package chess.moveCalculators;
import chess.*;
import java.util.Collection;
import java.util.ArrayList;

public class KnightMovesCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPiece myPiece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();

        int[] rowMoves = {2,2,1,1,-2,-2,-1,-1};
        int[] colMoves = {1,-1,2,-2,1,-1,2,-2};

        for (int i = 0; i < rowMoves.length; i++) {
            int updatedRow = row + rowMoves[i];
            int updatedCol = col + colMoves[i];

            ChessPosition newPosition = new ChessPosition(updatedRow, updatedCol);
            if(PieceMovesCalculator.isValidMove(board,position, newPosition)){
                ChessMove move = new ChessMove(position, newPosition, null);
                moves.add(move);
            }
            else{
                continue;
            }

        }
        return moves;
    }
}
