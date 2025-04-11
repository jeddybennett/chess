package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static boolean shouldHighlight = false;
    private static Collection<ChessPosition> currentValidMoves = null;
    private static chess.ChessPosition mySquare = null;

    public static void drawBoard(chess.ChessBoard board, boolean isWhite){
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
        drawHeaders(isWhite, out);
        if(isWhite){
            for(int row = 8; row>=1;row --){
                drawRow(out, board, row, true);
            }
        }
        else{
            for(int row = 1; row<=8;row ++){
                drawRow(out, board, row, false);
            }
        }
        drawHeaders(isWhite, out);
    }
    private static void drawHeaders(boolean isWhite, PrintStream out) {
        out.print("   ");
        if (isWhite) {
            for (char letter = 'a'; letter <= 'h'; letter++) {
                out.print(" " + letter + " ");
            }
        }
        else {
            for(char letter = 'h'; letter>='a';letter--){
                out.print(" " + letter + " ");
            }
        }
        out.println();
    }


    private static void drawRow(PrintStream out, chess.ChessBoard board, int row, boolean isWhite){
        System.out.printf("%2d ", row);
        if(isWhite){
            for(int col = 1; col <= 8; col++){
                drawSquare(out, board, row, col);
            }
        }
        else{
            for(int col = 8; col >=1; col--){
                drawSquare(out, board, row, col);
            }
        }
        System.out.printf("%2d\n", row);
    }

    private static void drawSquare(PrintStream out, chess.ChessBoard board, int row, int col){
        ChessPosition position = new ChessPosition(row, col);
        ChessPiece piece = board.getPiece(position);


        boolean whiteSquare = ((row + col) % 2 == 0);

        if(whiteSquare){
            drawWhiteSquare(out, piece, position);
        }
        else{
            drawBlackSquare(out, piece, position);
        }
    }

    private static void drawBlackSquare(PrintStream out, ChessPiece piece, ChessPosition position){
        //check if you need to draw a piece in this method
        System.out.print(SET_BG_COLOR_BLUE);
        checkSquareHighlight(out, position);
        if(piece == null){
            out.print(EMPTY);
        }
        else{
            String pieceSymbol = symbolPiece(piece);
            out.print(pieceSymbol);
        }
        out.print(RESET_BG_COLOR);
    }

    private static void drawWhiteSquare(PrintStream out, ChessPiece piece, ChessPosition position){
        //pass in a matrix of where the pieces are located
        //have draw BlackSquares and drawWhiteSquares look up positions in the matrix
        out.print(SET_BG_COLOR_RED);
        checkSquareHighlight(out, position);
        if(piece == null){
            System.out.print(EMPTY);
        }
        else{
            String pieceSymbol = symbolPiece(piece);
            System.out.print(pieceSymbol);
        }
        out.print(RESET_BG_COLOR);
    }

    private static void checkSquareHighlight(PrintStream out, ChessPosition position) {
        boolean highlightSquare = shouldHighlight && currentValidMoves!=null &&
                currentValidMoves.contains(position);
        boolean chosenSquare = mySquare != null && mySquare.equals(position);
        if(chosenSquare){
            out.print(SET_BG_COLOR_MAGENTA);
        }
        if(highlightSquare){
            out.print(SET_BG_COLOR_YELLOW);
        }
    }

    public static void highlightPieceMoves(chess.ChessGame game, chess.ChessPosition position, boolean isWhite){
        chess.ChessBoard chessBoard = game.getBoard();
        Collection<ChessMove> moves = game.validMoves(position);
        // Assuming getEndPosition() gives the destination

        currentValidMoves = moves.stream()
                .map(ChessMove::getEndPosition) // Assuming getEndPosition() gives the destination
                .toList();

        mySquare = position;
        shouldHighlight = true;
        drawBoard(chessBoard, isWhite);
        shouldHighlight = false;
        mySquare = null;
    }



    private static String symbolPiece(ChessPiece piece){
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            String symbol = getPieceType(piece);
            return SET_TEXT_COLOR_WHITE + symbol + RESET_TEXT_COLOR;
        }
        else{
            String symbol = getPieceType(piece);
            return SET_TEXT_COLOR_BLACK + symbol + RESET_TEXT_COLOR;
        }

    }

    private static String getPieceType(ChessPiece piece) {
        return switch (piece.getPieceType()) {
            case KING -> EscapeSequences.BLACK_KING;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case PAWN -> EscapeSequences.BLACK_PAWN;
            case null -> EMPTY;
        };
    }
    
}
