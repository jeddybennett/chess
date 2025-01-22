package chess.MoveCalculators;

import java.util.ArrayList;
import java.util.Collection;
import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessGame;

public class KingMovesCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position){
        Collection<ChessMove> moves = new ArrayList<>();

        //The King can move to any adjacent square if not occupied by another piece, or it puts the player in check
        int row_moves = {-1, 0, 1};
        int col_moves = {-1, 0, 1};




    }

}
