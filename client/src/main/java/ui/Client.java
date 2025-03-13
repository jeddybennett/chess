package ui;
import net.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Client {


    //This shouldn't draw any of the chess board here
    //This is where you should write code to display certain menus
    //Check flags to see what level menu you're at (use an authToken)
    //This will help you determine where the user is at
    //play game and observe game should draw the chessBoard

    public static void drawHeaders(PrintStream out){

    }

    public static void drawHeader(PrintStream out, String headerText){

    }

    private static void printHeaderText(PrintStream out, String player){
        out.print(SET_BG_COLOR_RED);
        out.print(SET_TEXT_COLOR_MAGENTA);
        //look up ansi color code escape sequences to make your own
    }

    private static void drawChessBoard(PrintStream out){
        //for (int boardRow = 0; )
    }

    private static void drawSquareRows(PrintStream out){

    }

    //look for unicode chess characters



}
