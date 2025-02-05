package chess.MoveCalculators;
import chess.*;
import java.util.Collection;
import java.util.ArrayList;

public class QueenMovesCalc implements PieceMovesCalculator{

    private final RookMovesCalc rookMovesCalc = new RookMovesCalc();
    private final BishopMovesCalc bishopMovesCalc = new BishopMovesCalc();

    @Override
    public Collection<ChessMove>pieceMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove>moves = new ArrayList<>();
        Collection<ChessMove>rookMoves = rookMovesCalc.pieceMoves(board, position);
        Collection<ChessMove>bishopMoves = bishopMovesCalc.pieceMoves(board, position);

        moves.addAll(rookMoves);
        moves.addAll(bishopMoves);

        return moves;
    }
}
