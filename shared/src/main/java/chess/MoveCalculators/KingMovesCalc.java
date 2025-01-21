package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalc implements PieceMovesCalculator{
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position, ChessGame.TeamColor team_color){
        Collection<ChessMove> moves = new ArrayList<>();

        //The King can move to any adjacent square if not occupied by another piece, or it puts the player in check
        int row_moves = {-1, 0, 1};
        int col_moves = {-1, 0, 1};




    }

}
