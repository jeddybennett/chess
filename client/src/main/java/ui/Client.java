package ui;
import exception.ResponseException;
import model.RegisterRequest;
import model.RegisterResult;
import net.ServerFacade;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

public class Client {

    private final static boolean preLogin = true;
    private final ServerFacade serverFacade;
    private final String serverURL;


    public Client(String serverURL, Repl repl) {
        serverFacade = new ServerFacade(serverURL);
        this.serverURL = serverURL;
    }

    //This shouldn't draw any of the chess board here
    //This is where you should write code to display certain menus
    //Check flags to see what level menu you're at (use an authToken)
    //This will help you determine where the user is at
    //play game and observe game should draw the chessBoard
    public void run(){
        System.out.println("Welcome to Chess: Register or Sign-in to Start");
//        System.out.println(client.help());
    }

    

    public String eval(String input) {
        String cmd;
        String[] params = null;
        if (preLogin) {
            var tokens = input.toLowerCase().split(" ");
            cmd = (tokens.length > 0) ? tokens[0] : "help";
            params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "register" -> register(params);
                case "login" -> login(params);
                case "quit" -> "quit";
                default -> helpPreLogin();
            };
        } else {
            return switch (cmd) {
                case "logout" -> logout(params);
                case "create" -> createGame(params);
                case "list" -> listGames(params);
                case "join" -> playGame(params);
                case "observe" -> observeGame(params);
                case "quit" -> "quit";
                default -> helpPostLogin();
            };
        }
    }

    public String register(String... params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        String email = params[2];
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult result = serverFacade.register(registerRequest);
        if(result != null && result.success())
    }

    public String login(String... params){

    }

    public String logout(String... params){

    }

    public String createGame(String... params){

    }

    public String listGames(String... params){

    }

    public String playGame(String... params){

    }

    public String observeGame(String... params){

    }

    public String helpPostLogin(){
            return """
                    create <NAME> - a game
                    list - games
                    join <ID> [WHITE|BLACK] - a game
                    logout - when you finish playing
                    quit - playing chess
                    help - with possible commands
                    """;
        }

    public String helpPreLogin() {
            return """
               Login as an Existing User: "login" <USERNAME> <PASSWORD>
               Register a new User: "register" <USERNAME> <PASSWORD> <EMAIL>
               Exit the program: "quit"
               Print this message: "help"
               """;
        }

}
