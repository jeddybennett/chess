package chess.movecalculators;

import chess.*;
import java.util.Collection;
import java.util.ArrayList;

public interface PieceMovesCalculator {
    Collection<ChessMove>pieceMoves(ChessBoard board, ChessPosition position);

    static Collection<ChessMove> rookAndBishopMoves(ChessBoard board, ChessPosition position, int[]
                                                    rowMoves, int[] colMoves){

        ArrayList<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int col = position.getColumn();

        for (int i = 0; i < rowMoves.length; i++){
            int currentRow = row;
            int currentCol = col;

            int rowMove = rowMoves[i];
            int colMove = colMoves[i];

            while(true){
                currentRow += rowMove;
                currentCol += colMove;

                ChessPosition newPosition = new ChessPosition(currentRow, currentCol);
                if(isValidMove(board, position, newPosition)){
                    if (finishedMoving(board, position, moves, newPosition)) {
                        break;
                    }
                }
                else{
                    break;
                }
            }
        }
        return moves;
    }


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
    static boolean finishedMoving(ChessBoard board, ChessPosition position, ArrayList<ChessMove> moves, ChessPosition newPosition) {
        ChessPiece otherPiece = board.getPiece(newPosition);
        if(otherPiece != null){
            ChessMove move = new ChessMove(position, newPosition, null);
            moves.add(move);
            return true;
        }
        else{
            ChessMove move = new ChessMove(position, newPosition, null);
            moves.add(move);
        }
        return false;
    }

}
