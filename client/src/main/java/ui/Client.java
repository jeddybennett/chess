package ui;
import chess.*;
import exception.ResponseException;
import model.*;
import net.ServerFacade;

import java.util.*;

public class Client {

    private static boolean preLogin = true;
    private static boolean inGame = false;
    private final ServerFacade serverFacade;
    private final String serverURL;
    private String authToken;
    private ChessGame activateGame;
    private static boolean isWhite = false;
    private final Map<Integer, GameData> gameMap = new HashMap<>();

    public Client(String serverURL, Repl repl) {
        serverFacade = new ServerFacade(serverURL);
        this.serverURL = serverURL;
    }

    public String eval(String input) throws ResponseException, InvalidMoveException {
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
        else if (inGame) {
            return switch (cmd) {
                case "redraw" -> {
                    if (params.length == 0) {
                        yield redrawGame(params);
                    }
                    else {
                        yield "Invalid Format. Type 'redraw' to redraw the current board.";
                    }
                }

                case "leave" -> {
                    if (params.length == 0) {
                        yield leaveGame(params);
                    }
                    else {
                        yield "Invalid Format. Type 'leave' to leave the current game.";
                    }

                }

                case "move" -> {
                    if (params.length == 2 || params.length == 3){
                        yield movePiece(params);
                    } else{
                        yield "Invalid Format. Type 'move' with the starting and ending square";
                    }
                }

                case "resign" -> {
                    if(params.length == 0){
                        yield resignGame(params);
                    }
                    else{
                        yield "Invalid Format. Type 'resign' in order to LOSE";
                    }
                }

                case "highlight" -> {
                    if(params.length == 2){
                        yield highlightMoves(params);
                    }
                    else{
                        yield "Invalid Format. Type 'highlight' followed by the location of the piece you want to learn about";
                    }
                }

                case "help" -> {
                    if(params.length == 0){
                        yield helpInGame();
                    }
                    else{
                        yield "Invalid Format. Type 'help' to learn about valid options";
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
        isWhite = !color.equals(ChessGame.TeamColor.BLACK);
        ChessBoard.drawBoard(board, isWhite);
        inGame = true;
        activateGame = newGame;
        return "Game joined Successfully";
    }

    public String observeGame(String... params){
        int gameNumber = Integer.parseInt(params[0]);
        GameData gameInfo = gameMap.get(gameNumber);
        chess.ChessBoard board = gameInfo.game().getBoard();
        ChessBoard.drawBoard(board, true);
        return "Now Observing Game";
    }

    public String redrawGame(String... params){
        chess.ChessBoard chessBoard = activateGame.getBoard();
        ChessBoard.drawBoard(chessBoard, isWhite);
        return "board has been redrawn";
    }

    public String leaveGame(String... params){
        inGame = false;
        return "ugh";
    }

    public String movePiece(String... params) throws InvalidMoveException {
        chess.ChessBoard board = activateGame.getBoard();
        String start = params[0];
        String finish = params[1];
        ChessPiece.PieceType promotionPiece = null;
        if(params.length == 3){
            promotionPiece = ChessPiece.PieceType.valueOf(params[2].toUpperCase());
        }
        int rowStart = getRowFromString(start);
        int colStart = getColFromString(start);

        int rowFinish = getRowFromString(finish);
        int colFinish = getColFromString(finish);

        ChessPosition startPosition = new ChessPosition(rowStart, colStart);
        ChessPosition endPosition = new ChessPosition(rowFinish, colFinish);

        //Make sure to check if pawn is being promoted or not, and if so, prompt user to
        // specify which promotion piece they want
        ChessPiece myPiece = board.getPiece(startPosition);
        ChessMove newMove;
        if(myPiece.getPieceType().equals(ChessPiece.PieceType.PAWN) && (rowFinish == 0 || rowFinish == 7)){
            newMove = new ChessMove(startPosition, endPosition, promotionPiece);
            String message
        }
        else{
            newMove = new ChessMove(startPosition, endPosition, null);
        }


        activateGame.makeMove(newMove);

        return "move made successfully";
    }

    public String resignGame(String... params){
        inGame = false;
        return "YOU LOST, HAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAH";
    }

    public String highlightMoves(String... params){
        int rowNum = getRowFromString(params[0]);
        int colNum = getColFromString(params[0]);

        ChessPosition position = new ChessPosition(rowNum, colNum);
        ChessBoard.highlightPieceMoves(activateGame, position);
        return "look and make a move now";
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

    public String helpInGame() {
        return """
                Redraw the current chess board: "redraw"
                Leave the current game: "leave"
                Make a move: "move" <Starting Square> <Ending Square> <Promotion Piece if promoting pawn>
                Resign but remain in the game: "resign"
                Highlight possible moves of an individual piece: "highlight" <Square where piece is located>
                """;
    }

    public static int getRowFromString(String square){
        return 8 - Character.getNumericValue(square.charAt(1));
    }

    public static int getColFromString(String square){
        return square.charAt(square.charAt(0)) - 'a';
    }

}
