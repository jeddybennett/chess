package ui;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.ERASE_SCREEN;

public class ChessBoard {

    //plan on deleting this after you get the chessboard working
    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
    }

    //Make an Enum for Black and White ChessColor
    //Build White first and then figure out how to draw for black
    public static void drawWhiteSquare(){
        //check if you need to draw a piece in this method
    }

    public static void drawBlackSquare(){
        //check if you need to draw a piece in this method
    }

    public static void drawChessBoard(){
        //pass in a matrix of where the pieces are located
        //have draw BlackSquares and drawWhiteSquares look up positions in the matrix
    }
}
