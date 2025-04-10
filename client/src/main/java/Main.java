import chess.*;
import exception.ResponseException;
import ui.*;

public class Main {
    public static void main(String[] args) throws ResponseException {
        var serverURL = "http://localhost:8080";
        if(args.length == 1){
            serverURL = args[0];
        }
         new Repl(serverURL).run();
    }
}