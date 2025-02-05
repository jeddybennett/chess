package chess.MoveCalculators;

import chess.*;
import java.util.Collection;
import java.util.ArrayList;

public interface PieceMovesCalculator {
    Collection<ChessMove>pieceMoves(ChessBoard board, ChessPosition position);

    static Boolean isValidMove(ChessBoard board, ChessPosition myPosition, ChessPosition targetPosition){
        if(!board.isOnBoard(targetPosition)){
            return false;
        }
        else{
            ChessPiece myPiece = board.getPiece(myPosition);
            ChessPiece otherPiece = board.getPiece(targetPosition);

            if(otherPiece == null){
                return true;
            }
            else if(otherPiece.getTeamColor() != myPiece.getTeamColor()){
                return true;
            }
            else{
                return false;
            }
        }
    }
}
