package chess.MoveCalculators;

import java.util.ArrayList;
import java.util.Collection;

import chess.*;

public class KingMovesCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> moves = new ArrayList<>();

        int row = position.getRow();
        int column = position.getColumn();

        ChessPiece my_piece = board.getPiece(position);

        //The King can move to any adjacent square if not occupied by another piece, or it puts the player in check
        int [] row_moves = {-1, 0, 1};
        int [] col_moves = {-1, 0, 1};

        for (int i = 0; i < row_moves.length; i++){
            for (int j = 0; j < col_moves.length; j++){
                int row_move = row_moves[i] + row;
                int col_move = col_moves[j] + column;

                ChessPosition new_position = new ChessPosition(row_move, col_move);
                if(!board.isOnBoard(board, new_position)){
                    continue;
                }
                else if(PieceMovesCalculator.isValidMove(board, position, new_position)){
                    ChessPiece piece = board.getPiece(new_position);
                    if(piece != null && piece.getTeamColor() != my_piece.getTeamColor()){
                        ChessMove move = new ChessMove(position, new_position, null);
                        moves.add(move);
                    }
                    else if (piece == null){
                        ChessMove move = new ChessMove(position, new_position, null);
                        moves.add(move);
                    }
                }
            }

        }
        return moves;

    }

}
