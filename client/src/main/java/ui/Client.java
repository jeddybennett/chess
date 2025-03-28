package ui;
import chess.ChessGame;
import exception.ResponseException;
import model.*;
import net.ServerFacade;

import java.util.*;

public class Client {

    private static boolean preLogin = true;
    private final ServerFacade serverFacade;
    private final String serverURL;
    private String authToken;
    private final Map<Integer, GameData> gameMap = new HashMap<>();

    public Client(String serverURL, Repl repl) {
        serverFacade = new ServerFacade(serverURL);
        this.serverURL = serverURL;
    }

    public String eval(String input) throws ResponseException {
        var tokens = input.split(" ");
        String cmd = (tokens.length > 0) ? tokens[0].toLowerCase() : "help";
        String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        if (preLogin) {
            return switch (cmd) {
                case "register" -> {
                    if (params.length != 3) {
                        yield "Invalid format. Usage: register <USERNAME> <PASSWORD> <EMAIL>";
                    } else {
                        yield register(params);
                    }
                }
                case "login" -> {
                    if (params.length != 2) {
                        yield "Invalid format. Usage: login <USERNAME> <PASSWORD>";
                    } else {
                        yield login(params);
                    }
                }
                case "quit" -> {
                    if(params.length == 0){
                        yield "quit";
                    }
                    else{
                        yield "Invalid format. Just type 'quit' with no arguments.";
                    }
                }

                case "help" -> {
                    if (params.length == 0) {
                        yield helpPreLogin();
                    } else {
                        yield "Invalid format. Just type 'help' with no arguments.";
                    }
                }
                default -> "Invalid Entry. Type 'help' to learn about valid options.";
            };
        }
        else {
            return switch (cmd) {
                case "logout" -> {
                    if (params.length != 0) {
                        yield "Invalid format. Just type 'logout'.";
                    } else {
                        yield logout(params);
                    }
                }
                case "list" -> {
                    if (params.length != 0) {
                        yield "Invalid format. Just type 'list'.";
                    } else {
                        yield listGames(params);
                    }
                }
                case "create" -> {
                    if (params.length != 1) {
                        yield "Invalid format. Usage: create <GAME_NAME>";
                    } else {
                        yield createGame(params);
                    }
                }
                case "join" -> {
                    if (params.length != 2) {
                        yield "Invalid format. Usage: join <GAME_NUMBER> [WHITE|BLACK]";
                    } else {
                        yield playGame(params);
                    }
                }
                case "observe" -> {
                    if (params.length != 1) {
                        yield "Invalid format. Usage: observe <GAME_NUMBER>";
                    } else {
                        yield observeGame(params);
                    }
                }
                case "quit" -> {
                    if(params.length == 0){
                        yield "quit";
                    }
                    else{
                        yield "Invalid format. Just type 'quit' with no arguments.";
                    }
                }
                case "help" -> {
                    if (params.length == 0) {
                        yield helpPostLogin();
                    } else {
                        yield "Invalid format. Just type 'help' with no arguments.";
                    }
                }
                default -> "Invalid Entry. Type 'help' to learn about valid options.";
            };
        }
    }

    public boolean isLogin(){
        return preLogin;
    }

    public String register(String... params) throws ResponseException {
        String username = params[0];
        String password = params[1];
        String email = params[2];
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        RegisterResult result = serverFacade.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = serverFacade.login(loginRequest);
        authToken = loginResult.authToken();
        if(result != null && authToken != null){
            preLogin = false;
            return "User Registered and Logged in Successfully";
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
        authToken = loginResult.authToken();
        if(loginResult.authToken() != null){
            preLogin = false;
            return "Login Successful";
        }
        else{
            return "Login failed";
        }
    }

    public String logout(String... params) throws ResponseException {
        serverFacade.logout(authToken);
        preLogin = true;
        authToken = null;
        return "Logged out successfully";
    }

    public String createGame(String... params) throws ResponseException {
        String gameName = params[0];
        CreateGameRequest createGameRequest = new CreateGameRequest(gameName, authToken);
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
        ListGameRequest listGameRequest = new ListGameRequest(authToken);
        ListGameResult result = serverFacade.listGames(listGameRequest);
        Collection<GameData>games = result.games();
        if(games.isEmpty()){
            return "There are no Games created";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Current Games\n");
        int i = 1;
        for(GameData game: games){
            sb.append(i).append(") ").append(game.gameName()).append("\n");
            sb.append("    WHITE PLAYER: ").append(game.whiteUsername() != null ? game.whiteUsername() + "\n" : "none\n");
            sb.append("    BLACK PLAYER: ").append(game.blackUsername() != null ? game.blackUsername() + "\n" : "none\n");
            gameMap.put(i, game);
            i++;
        }

        return sb.toString();
    }

    public String playGame(String... params) throws ResponseException {
        int gameNumber = Integer.parseInt(params[0]);
        ChessGame.TeamColor color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());
        GameData gameInfo = gameMap.get(gameNumber);
        int gameID = gameInfo.gameID();
        ChessGame newGame = gameInfo.game();
        chess.ChessBoard board = newGame.getBoard();
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, color, gameID);
        serverFacade.joinGame(joinGameRequest);
        boolean isWhite = !color.equals(ChessGame.TeamColor.BLACK);
        ChessBoard.drawBoard(board, isWhite);
        return "Game joined Successfully";
    }

    public String observeGame(String... params){
        int gameNumber = Integer.parseInt(params[0]);
        GameData gameInfo = gameMap.get(gameNumber);
        chess.ChessBoard board = gameInfo.game().getBoard();
        ChessBoard.drawBoard(board, true);
        return "Now Observing Game";
    }

    public String helpPostLogin(){
            return """
                    create <NAME> - a game to be played
                    list - games
                    join <ID> [WHITE|BLACK] - play a game
                    observe <ID> - watch a game
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
