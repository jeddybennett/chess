package ui;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {

    private static final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    public static void drawBoard(chess.ChessBoard board, boolean isWhite){

        out.print(ERASE_SCREEN);
        drawHeaders(out, isWhite);
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
        drawHeaders(out, isWhite);
    }
    private static void drawHeaders(PrintStream out, boolean isWhite) {
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
            drawWhiteSquare(out, piece);
        }
        else{
            drawBlackSquare(out, piece);
        }
    }

    private static void drawBlackSquare(PrintStream out, ChessPiece piece){
        //check if you need to draw a piece in this method
        System.out.print(SET_BG_COLOR_BLUE);
        if(piece == null){
            out.print(EMPTY);
        }
        else{
            String pieceSymbol = symbolPiece(piece);
            out.print(pieceSymbol);
        }
        out.print(RESET_BG_COLOR);
    }

    private static void drawWhiteSquare(PrintStream out, ChessPiece piece){
        //pass in a matrix of where the pieces are located
        //have draw BlackSquares and drawWhiteSquares look up positions in the matrix
        out.print(SET_BG_COLOR_RED);
        if(piece == null){
            System.out.print(EMPTY);
        }
        else{
            String pieceSymbol = symbolPiece(piece);
            System.out.print(pieceSymbol);
        }
        out.print(RESET_BG_COLOR);
    }

    public static void highlightPieceMoves(chess.ChessGame game, chess.ChessPosition position){
        out.print(SET_BG_COLOR_YELLOW);
        chess.ChessBoard chessBoard = game.getBoard();


    }



    private static String symbolPiece(ChessPiece piece){
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return getPieceType(piece, WHITE_KING, WHITE_QUEEN, WHITE_ROOK, WHITE_BISHOP, WHITE_KNIGHT, WHITE_PAWN);
        }
        else{
            return getPieceType(piece, BLACK_KING, BLACK_QUEEN, BLACK_ROOK, BLACK_BISHOP, BLACK_KNIGHT, BLACK_PAWN);
        }

    }

    private static String getPieceType(ChessPiece piece, String blackKing, String blackQueen, String blackRook,
                                       String blackBishop, String blackKnight, String blackPawn) {
        return switch (piece.getPieceType()) {
            case KING -> blackKing;
            case QUEEN -> blackQueen;
            case ROOK -> blackRook;
            case BISHOP -> blackBishop;
            case KNIGHT -> blackKnight;
            case PAWN -> blackPawn;
            case null -> EMPTY;
        };
    }
    
}
