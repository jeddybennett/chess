package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class ChessBoard {

    //plan on deleting this after you get the chessboard working
    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
    }

    public enum ChessColor{
        WHITE,
        BLACK
    }

    static class ChessSquare{
        ChessColor color;
        ChessPiece.PieceType pieceType;
        boolean whiteTeam;
        boolean startGame;

        ChessSquare(ChessColor color, ChessPiece.PieceType pieceType, boolean whiteTeam,
                    boolean startGame){
            this.color = color;
            this.pieceType = pieceType;
            this.whiteTeam = whiteTeam;
            this.startGame = startGame;
        }
    }
    //Build White first and then figure out how to draw for black
    public static void drawWhiteSquare(ChessPiece piece){
        //check if you need to draw a piece in this method
        String color = SET_BG_COLOR_RED;
        if(piece == null){
            System.out.println();
        }
    }

    private static void drawRow(){

    }

    private static void drawSquare(){
        
    }

    private static void drawBlackSquare(ChessPiece piece){
        //check if you need to draw a piece in this method
        System.out.print(SET_BG_COLOR_BLUE);
        if(piece == null){
            System.out.print(EMPTY);
        }
        else{
            String pieceSymbol = symbolPiece(piece);
            System.out.print(pieceSymbol);
        }
        System.out.print(RESET_BG_COLOR);
    }

    private static void drawChessBoard(ChessPiece piece){
        //pass in a matrix of where the pieces are located
        //have draw BlackSquares and drawWhiteSquares look up positions in the matrix
        System.out.print(SET_BG_COLOR_RED);
        if(piece == null){
            System.out.print(EMPTY);
        }
        else{
            String pieceSymbol = symbolPiece(piece);
            System.out.print(pieceSymbol);
        }
        System.out.print(RESET_BG_COLOR);
    }



    private static String symbolPiece(ChessPiece piece){
        if(piece.getTeamColor() == ChessGame.TeamColor.WHITE){
            return getPieceType(piece, WHITE_KING, WHITE_QUEEN, WHITE_ROOK, WHITE_BISHOP, WHITE_KNIGHT, WHITE_PAWN);
        }
        else{
            return getPieceType(piece, BLACK_KING, BLACK_QUEEN, BLACK_ROOK, BLACK_BISHOP, BLACK_KNIGHT, BLACK_PAWN);
        }

    }

    private static String getPieceType(ChessPiece piece, String blackKing, String blackQueen, String blackRook, String blackBishop, String blackKnight, String blackPawn) {
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
