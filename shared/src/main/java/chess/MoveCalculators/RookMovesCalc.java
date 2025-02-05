package chess.MoveCalculators;
import chess.*;
import java.util.Collection;
import java.util.ArrayList;
public class RookMovesCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPiece myPiece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();

        int[] row_moves = {1, -1, 0, 0};
        int[] col_moves = {0, 0, 1, -1};

        for (int i = 0; i < row_moves.length; i++){
            int current_row = row;
            int current_col = col;

            int row_move = row_moves[i];
            int col_move = col_moves[i];

            while(true){
                current_row += row_move;
                current_col += col_move;

                ChessPosition newPosition = new ChessPosition(current_row, current_col);
                if(PieceMovesCalculator.isValidMove(board, position, newPosition)){
                    ChessPiece otherPiece = board.getPiece(newPosition);
                    if(otherPiece != null){
                        ChessMove move = new ChessMove(position, newPosition, null);
                        moves.add(move);
                        break;
                    }
                    else{
                        ChessMove move = new ChessMove(position, newPosition, null);
                        moves.add(move);
                    }
                }
                else{
                    break;
                }
            }
        }
        return moves;
    }
}