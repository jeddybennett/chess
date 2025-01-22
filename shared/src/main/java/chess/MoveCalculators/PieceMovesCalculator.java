package chess.MoveCalculators;
import chess.*;

import java.util.Collection;

public interface PieceMovesCalculator {
    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position);

    static boolean isValidMove(ChessBoard board, ChessPosition currentPosition, ChessPosition targetPosition) {

        ChessPiece my_piece = board.getPiece(currentPosition);
        ChessPiece other_piece = board.getPiece(targetPosition);

        if (other_piece == null){
            return true;
        }
        else if(my_piece.getTeamColor() != other_piece.getTeamColor()){
            return true;
        }
        else{
            return false;
        }
    }




}
