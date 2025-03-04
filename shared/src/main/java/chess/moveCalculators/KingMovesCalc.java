package chess.moveCalculators;
import chess.*;
import java.util.Collection;
import java.util.ArrayList;
public class KingMovesCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();

        int [] rowMoves = {-1, 0, 1};
        int [] colMoves = {-1, 0, 1};

        for(int i = 0; i < rowMoves.length; i++){
            for(int j = 0; j < rowMoves.length; j++){
                int updatedRow = row + rowMoves[i];
                int updatedCol = col + colMoves[j];

                ChessPosition newPosition = new ChessPosition(updatedRow, updatedCol);
                if(PieceMovesCalculator.isValidMove(board, position, newPosition)){
                    ChessMove move = new ChessMove(position, newPosition, null);
                    moves.add(move);
                }
                else{
                    continue;
                }
            }
        }
        return moves;
    }
}
