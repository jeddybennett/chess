package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor team_color;
    private ChessBoard board;

    public ChessGame() {
        setTeamTurn(TeamColor.WHITE);
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.team_color;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team_color = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    public void movePiece(ChessPosition start, ChessPosition end){
        ChessPiece myPiece = board.getPiece(start);
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece startPiece = board.getPiece(startPosition);
        if (startPiece == null) {
            return new ArrayList<>();
        }

        Collection <ChessMove> moves = startPiece.pieceMoves(board, startPosition);
        Collection <ChessMove> validMoves = new ArrayList<>();
        for(ChessMove move: moves){
            ChessBoard newBoard = board.copyBoard();


        }
        return startPiece.pieceMoves(board, startPosition);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece myPiece = board.getPiece(start);

        if (myPiece == null){
            throw new InvalidMoveException("No Piece located here");
        }

        if(myPiece.getTeamColor() != getTeamTurn()){
            throw new InvalidMoveException("It is " + getTeamTurn() + "'s"+  " turn");

        }




    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if(!isInCheck(teamColor)){
            return false;
        }

    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)){
            return false;
        }
        else{

        }

    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }

    private ChessPosition findKing(TeamColor team, ChessBoard board){
        int size = 8;
        for (int row = 1; row <=size; row ++){
            for (int col = 1; col <=size; col++){
                ChessPosition kingPosition = new ChessPosition(row, col);
                ChessPiece myPiece = board.getPiece(kingPosition);
                if(myPiece.getPieceType() == ChessPiece.PieceType.KING && myPiece.getTeamColor() == team){
                    return kingPosition;
                }

            }
        }
        return null;
    }

    private Boolean leaveKingInCheck(TeamColor team, ChessBoard board){
        ChessPosition kingPosition = findKing(team, board);

        int size = 8;
        for(int row = 1; row <= size; row ++){
            for(int col = 1; col<= size; col++){
                ChessPosition newPosition = new ChessPosition(row, col);
                ChessPiece enemyPiece = board.getPiece(newPosition);
                if(enemyPiece != null && enemyPiece.getTeamColor() != team){
                    Collection<ChessMove>enemyMoves = enemyPiece.pieceMoves(board, newPosition);
                    for(ChessMove move: enemyMoves){
                        if(move.getEndPosition() == kingPosition){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

}
