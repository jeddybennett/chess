package chess.MoveCalculators;

import java.util.ArrayList;
import java.util.Collection;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;

public class BishopMovesCalc implements PieceMovesCalculator{

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position){
        Collection<ChessMove> moves = new ArrayList<>();
        ChessPiece my_piece = chessBoard.getPiece(position);

        int row = position.getRow();
        int column = position.getColumn();

        int [] row_moves = {-1,-1,1,1};
        int [] column_moves = {1,-1,-1,1};

        for (int i = 0; i < row_moves.length; i++){
            int row_move = row_moves[i];
            int column_move = column_moves[i];

            int current_row = row;
            int current_column = column;

            while(true){
                current_row += row_move;
                current_column += column_move;

                ChessPosition new_position = new ChessPosition(current_row, current_column);

                if (!chessBoard.isOnBoard(chessBoard, new_position)) {
                    break; // Stop if the position is off the board
                }

                ChessPiece new_piece = chessBoard.getPiece(new_position);

                if(PieceMovesCalculator.isValidMove(chessBoard, position, new_position)){
                    if(new_piece == null){
                        ChessMove move = new ChessMove(position,new_position, null);
                        moves.add(move);
                    }
                    else if(new_piece.getTeamColor() != my_piece.getTeamColor()){
                        ChessMove move = new ChessMove(position,new_position, null);
                        moves.add(move);
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
}
