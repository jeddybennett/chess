package ui;
import net.ServerFacade;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Client {

    public static void main(String[] args){
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(ERASE_SCREEN);
    }

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



}
