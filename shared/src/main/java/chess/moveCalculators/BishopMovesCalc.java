package chess.moveCalculators;
import chess.*;
import java.util.Collection;
import java.util.ArrayList;

public class BishopMovesCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        int row = position.getRow();
        int col = position.getColumn();

        int[] rowMoves = {1, 1, -1, -1};
        int[] colMoves = {1, -1, 1, -1};

        for (int i = 0; i < rowMoves.length; i++){
            int currentRow = row;
            int currentCol = col;

            int rowMove = rowMoves[i];
            int colMove = colMoves[i];

            while(true){
                currentRow += rowMove;
                currentCol += colMove;

                ChessPosition newPosition = new ChessPosition(currentRow, currentCol);
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

