package chess.MoveCalculators;

import java.util.ArrayList;
import java.util.Collection;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;

public class KnightMovesCalc implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();
        int [] rows_moves = {2,2,-2,-2,1,1,-1,-1};
        int [] col_moves = {1,-1,1,-1,2,-2,2,-2,};

        for (int i = 0; i < rows_moves.length; i++) {
            int updated_row = rows_moves[i] + row;
            int updated_column = column + col_moves[i];
            ChessPosition newPosition = new ChessPosition(updated_row, updated_column);
            if (!chessBoard.isOnBoard(chessBoard, newPosition)) {
                continue;
            }
            else if (!PieceMovesCalculator.isValidMove(chessBoard, position, newPosition)){
                continue;
            }
            else{
                ChessMove new_move = new ChessMove(position, newPosition, null);
                moves.add(new_move);
            }
        }
    return moves;

    }
}
