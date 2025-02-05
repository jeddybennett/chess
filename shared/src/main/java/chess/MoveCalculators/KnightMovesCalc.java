package chess.MoveCalculators;
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

        int[] row_moves = {2,2,1,1,-2,-2,-1,-1};
        int[] col_moves = {1,-1,2,-2,1,-1,2,-2};

        for (int i = 0; i < row_moves.length; i++) {
            int updated_row = row + row_moves[i];
            int updated_col = col + col_moves[i];

            ChessPosition newPosition = new ChessPosition(updated_row, updated_col);
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
