package ui;
import chess.ChessGame;
import exception.ResponseException;
import model.*;
import net.ServerFacade;

import java.util.*;

public class Client {

    private enum State{
        LOGGED_OUT,
        LOGGED_IN
    }

    private static boolean preLogin = true;
    private final ServerFacade serverFacade;
    private final String serverURL;
    private State state;
    private String authToken;


    public Client(String serverURL, Repl repl) {
        serverFacade = new ServerFacade(serverURL);
        this.serverURL = serverURL;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    //This shouldn't draw any of the chess board here
    //This is where you should write code to display certain menus
    //Check flags to see what level menu you're at (use an authToken)
    //This will help you determine where the user is at
    //play game and observe game should draw the chessBoard
//    public void run(){
//        System.out.println("Welcome to Chess: Register or Sign-in to Start");
////        System.out.println(client.help());
//    }



    public String eval(String input) throws ResponseException {
        var tokens = input.toLowerCase().split(" ");
        String cmd = (tokens.length > 0) ? tokens[0] : "help";;
        String[] params = null;
        if (preLogin) {
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
        if(result != null){
            return "User Registered Successfully";
        }
        else{
            return "User registration invalid";
        }
    }

    public String login(String... params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = serverFacade.login(loginRequest);
        this.authToken = loginResult.authToken();
        if(loginResult.authToken() != null){
            preLogin = false;
            return "Login Successful";
        }
        else{
            return "Login failed";
        }
    }

    public String logout(String... params) throws ResponseException {
        LogoutRequest logoutRequest = new LogoutRequest(this.authToken);
        serverFacade.logout(logoutRequest);
        preLogin = true;
        this.authToken = null;
        return "Logged out successfully";
    }

    public String createGame(String... params) throws ResponseException {
        String gameName = params[0];
        String newAuth = generateToken();
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName, newAuth);
        CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
        int gameID = createGameResult.gameID();
        if(gameID > 0){
            return "Game Created Successfully";
        }
        else{
            return "Game not created";
        }


    }

    public String listGames(String... params) throws ResponseException {
        ListGameRequest listGameRequest = new ListGameRequest(this.authToken);
        ListGameResult result = serverFacade.listGames(listGameRequest);
        Collection<GameData>games = result.games();
        return games.toString();
    }

    public String playGame(String... params) throws ResponseException {

        String newAuth = generateToken();
        ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(params[0].toUpperCase());
        int newGameID = Integer.parseInt(params[1]);

        JoinGameRequest joinGameRequest = new JoinGameRequest(newAuth, color, newGameID);
        serverFacade.joinGame(joinGameRequest);
        return "Game joined Successfully";
    }

    public String observeGame(String... params){
        int gameID = Integer.parseInt(params[0]);
        return "Now Observing Game";
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
