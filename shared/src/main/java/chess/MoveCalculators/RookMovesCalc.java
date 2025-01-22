package chess.MoveCalculators;

import java.util.ArrayList;
import java.util.Collection;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessPiece;
import chess.ChessGame;

public class RookMovesCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPiece my_piece = board.getPiece(position);

        int row = position.getRow();
        int column = position.getColumn();

        int [] row_moves = {1 , -1, 0, 0};
        int [] col_moves = {0, 0, 1, -1};

        for (int i = 0; i < row_moves.length; i++){
            int row_move = row_moves[i];
            int col_move = col_moves[i];

            int current_row = row;
            int current_column = column;

            while(board.isOnBoard(board, new ChessPosition(current_row, current_column))){
                current_row += row_move;
                current_column += col_move;



                ChessPosition new_position = new ChessPosition(current_row, current_column);
                ChessPiece other_piece = board.getPiece(new_position);

                if (PieceMovesCalculator.isValidMove(board, position, new_position)) {
                    if (other_piece == null) {
                        ChessMove move = new ChessMove(position, new_position, null);
                        moves.add(move);
                    } else if(other_piece.getTeamColor() != my_piece.getTeamColor()) {
                        ChessMove move = new ChessMove(position, new_position, null);
                        moves.add(move);
                        break;
                    }
                }
            }
        }

        return moves;
    }
}
