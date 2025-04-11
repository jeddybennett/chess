package ui;
import chess.*;
import exception.ResponseException;
import model.*;
import net.ServerFacade;
import websocket.ServerMessageObserver;
import websocket.WebSocketFacade;


import java.util.*;

public class Client{

    private static boolean preLogin = true;
    private static boolean inGame = false;
    private static boolean isObserve = false;
    private final ServerFacade serverFacade;
    private String authToken;
    private static GameData activeGame;
    private static boolean isWhite = false;
    private final Map<Integer, GameData> gameMap = new HashMap<>();
    private final WebSocketFacade webSocketFacade;

    public Client(String serverURL, ServerMessageObserver repl) throws ResponseException {
        serverFacade = new ServerFacade(serverURL);
        webSocketFacade = new WebSocketFacade(serverURL, repl);
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
                        yield register(params);}}
                case "login" -> {
                    if (params.length != 2) {
                        yield "Invalid format. Usage: login <USERNAME> <PASSWORD>";}
                    else {
                        yield login(params);}}
                case "quit" -> {if(params.length == 0){
                        yield "quit";}
                    else{
                        yield "Invalid format. Just type 'quit' with no arguments.";}}
                case "help" -> {if (params.length == 0) {
                        yield helpPreLogin();}
                else {
                        yield "Invalid format. Just type 'help' with no arguments.";}}
                default -> "Invalid Entry. Type 'help' to learn about valid options.";};}
        else if (inGame) {
            return switch (cmd) {
                case "redraw" -> {
                    if (params.length == 0) {
                        yield redrawGame();}
                    else {
                        yield "Invalid Format. Type 'redraw' to redraw the current board.";
                    }}
                case "leave" -> {
                    if (params.length == 0) {
                        yield leaveGame(params);}
                    else {
                        yield "Invalid Format. Type 'leave' to leave the current game.";}}
                case "move" -> {
                    if (params.length == 2){
                        yield movePiece(params);}
                    else{
                        yield "Invalid Format. Type 'move' with the starting and ending square";}}
                case "resign" -> {
                    if(params.length == 0){
                        yield resignGame(params);
                    }
                    else{
                        yield "Invalid Format. Type 'resign' in order to LOSE";}}
                case "highlight" -> {
                    if(params.length == 1){
                        yield highlightMoves(params);}
                    else{
                        yield "Invalid Format. Type 'highlight' followed by the location of the piece you want to learn about";}}
                case "help" -> {
                    if(params.length == 0){
                        yield helpInGame();}
                    else{
                        yield "Invalid Format. Type 'help' to learn about valid options";}}
                default -> "Invalid Entry. Type 'help' to learn about valid options.";};}
        else {
            return switch (cmd) {
                case "logout" -> {
                    if (params.length != 0) {
                        yield "Invalid format. Just type 'logout'.";
                    } else {
                        yield logout(params);}}
                case "list" -> {
                    if (params.length != 0) {
                        yield "Invalid format. Just type 'list'.";
                    } else {
                        yield listGames(params);}}
                case "create" -> {
                    if (params.length != 1) {
                        yield "Invalid format. Usage: create <GAME_NAME>";
                    } else {
                        yield createGame(params);}}
                case "join" -> {
                    if (params.length != 2) {
                        yield "Invalid format. Usage: join <GAME_NUMBER> [WHITE|BLACK]";
                    } else {
                        yield playGame(params);}}
                case "observe" -> {
                    if (params.length != 1) {
                        yield "Invalid format. Usage: observe <GAME_NUMBER>";
                    } else {
                        yield observeGame(params);}}
                case "quit" -> {
                    if(params.length == 0){
                        yield "quit";}
                    else{
                        yield "Invalid format. Just type 'quit' with no arguments.";}}
                case "help" -> {
                    if (params.length == 0) {
                        yield helpPostLogin();
                    } else {
                        yield "Invalid format. Just type 'help' with no arguments.";}}
                default -> "Invalid Entry. Type 'help' to learn about valid options.";};}
    }

    public boolean isLogin(){
        return preLogin;
    }

    public boolean isInGame(){
        return inGame;
    }

    public String register(String... params){
        try {
            String username = params[0];
            String password = params[1];
            String email = params[2];
            RegisterRequest registerRequest = new RegisterRequest(username, password, email);
            RegisterResult result = serverFacade.register(registerRequest);
            LoginRequest loginRequest = new LoginRequest(username, password);
            LoginResult loginResult = serverFacade.login(loginRequest);
            authToken = loginResult.authToken();
            if (result != null && authToken != null) {
                preLogin = false;
                return "User Registered and Logged in Successfully";
            } else {
                return "User registration invalid";
            }
        }
        catch(Exception e){
            return "User Already Taken. Pick a different username.";
        }
    }

    public String login(String... params){
        try {
            String username = params[0];
            String password = params[1];
            LoginRequest loginRequest = new LoginRequest(username, password);
            LoginResult loginResult = serverFacade.login(loginRequest);
            authToken = loginResult.authToken();
            if (loginResult.authToken() != null) {
                preLogin = false;
                return "Login Successful";
            } else {
                return "Login failed";
            }
        }
        catch(Exception e){
            return "Login failed: invalid Username or Password.";
        }
    }

    public String logout(String... params) throws ResponseException {
        serverFacade.logout(authToken);
        preLogin = true;
        authToken = null;
        return "Logged out successfully";
    }

    public String createGame(String... params){
        try {
            String gameName = params[0];
            CreateGameRequest createGameRequest = new CreateGameRequest(gameName, authToken);
            CreateGameResult createGameResult = serverFacade.createGame(createGameRequest);
            int gameID = createGameResult.gameID();
            if (gameID > 0) {
                return "Game Created Successfully";
            } else {
                return "Game not created";
            }
        }
        catch(Exception e){
            return "Game Creation Failed. Try again";
        }


    }

    public String listGames(String... params){
        try {
            ListGameRequest listGameRequest = new ListGameRequest(authToken);
            ListGameResult result = serverFacade.listGames(listGameRequest);
            Collection<GameData> games = result.games();
            if (games.isEmpty()) {
                return "There are no Games created";
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Current Games\n");
            int i = 1;
            for (GameData game : games) {
                sb.append(i).append(") ").append(game.gameName()).append("\n");
                sb.append("    WHITE PLAYER: ").append(game.whiteUsername() != null ? game.whiteUsername() + "\n" : "none\n");
                sb.append("    BLACK PLAYER: ").append(game.blackUsername() != null ? game.blackUsername() + "\n" : "none\n");
                gameMap.put(i, game);
                i++;
            }

            return sb.toString();
        }
        catch(Exception e){
            return "List Games Failed. Try again";
        }
    }

    public String playGame(String... params){
        int gameNumber = Integer.parseInt(params[0]);
        try {
            ChessGame.TeamColor color;
            try {
                color = ChessGame.TeamColor.valueOf(params[1].toUpperCase());

            } catch (IllegalArgumentException e) {
                return params[1].toUpperCase() + " is not a valid color. Select WHITE or BLACK";
            }
            GameData gameInfo = gameMap.get(gameNumber);
            int gameID = gameInfo.gameID();
            ChessGame newGame = gameInfo.game();
            chess.ChessBoard board = newGame.getBoard();
            JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, color, gameID);
            try {
                serverFacade.joinGame(joinGameRequest);
            }
            catch(ResponseException e){
                return color + " is already taken";
            }
            webSocketFacade.connect(authToken, gameID);
            isWhite = !color.equals(ChessGame.TeamColor.BLACK);
            inGame = true;
            activeGame = gameInfo;
            return "Game joined Successfully";
        }
        catch(Exception e){
            return "game " + gameNumber + " does not exist. Make sure to list games to be sure.";
        }
    }

    public String observeGame(String... params){
        int gameNumber = Integer.parseInt(params[0]);
        try {
            GameData gameInfo = gameMap.get(gameNumber);
            chess.ChessBoard board = gameInfo.game().getBoard();
            webSocketFacade.connect(authToken, gameInfo.gameID());
            inGame = true;
            isObserve = true;
            return "Now Observing Game";
        }
        catch(Exception e){
            return "game " + gameNumber + " does not exist. Make sure to list games to be sure.";
        }
    }

    public static String redrawGame(){
        chess.ChessBoard chessBoard = activeGame.game().getBoard();
        if(isObserve){
            ChessBoard.drawBoard(chessBoard, true);
        }
        else{
            ChessBoard.drawBoard(chessBoard, isWhite);
        }
        return "board has been redrawn";
    }

    public String leaveGame(String... params) throws ResponseException {
        inGame = false;
        if(isObserve){
            isObserve = false;
        }
        webSocketFacade.leave(authToken, activeGame.gameID());
        return "don't leave me";
    }

    public String movePiece(String... params) throws InvalidMoveException, ResponseException {
        try {
            chess.ChessBoard board = activeGame.game().getBoard();
            String start = params[0];
            String finish = params[1];
            int rowStart = getRowFromString(start);
            int colStart = getColFromString(start);

            int rowFinish = getRowFromString(finish);
            int colFinish = getColFromString(finish);

            ChessPosition startPosition = new ChessPosition(rowStart, colStart);
            ChessPosition endPosition = new ChessPosition(rowFinish, colFinish);

            //Make sure to check if pawn is being promoted or not, and if so, prompt user to
            // specify which promotion piece they want
            ChessPiece myPiece = board.getPiece(startPosition);
            if (myPiece == null) {
                return "There is not piece at selected starting Position. Choose another Position.";
            }

            ChessMove newMove;
            String message;
            boolean isPawn = myPiece.getPieceType().equals(ChessPiece.PieceType.PAWN);
            boolean endOfBoard = (rowFinish == 1 || rowFinish == 8);
            if (isPawn && endOfBoard) {
                System.out.print("Which Piece do you want to promote to (Queen/Rook/Bishop/Knight): ");
                Scanner scanner = new Scanner(System.in);
                ChessPiece.PieceType promotionPiece;
                while (true) {
                    String line = scanner.nextLine().trim().toUpperCase();
                    try {
                        ChessPiece.PieceType pieceType = ChessPiece.PieceType.valueOf(line);
                        if (pieceType == ChessPiece.PieceType.PAWN || pieceType == ChessPiece.PieceType.KING) {
                            throw new IllegalArgumentException();
                        }
                        promotionPiece = pieceType;
                        break;
                    } catch (Exception e) {
                        System.out.print("Invalid Piece. Please Type Queen, Rook, Bishop, Knight: ");
                    }
                }
                newMove = new ChessMove(startPosition, endPosition, promotionPiece);
                message = "Promoting your pawn to " + promotionPiece;
            } else {
                newMove = new ChessMove(startPosition, endPosition, null);
                message = "move made successfully";
            }
            activeGame.game().makeMove(newMove);
            webSocketFacade.makeMove(authToken, activeGame.gameID(), newMove);

            return message;
        }
        catch(Exception e){
            return "Invalid Move. Please type <starting square> <ending square>";
        }
    }

    public static void updateGame(GameData game) {
        activeGame = game;
    }


    public String resignGame(String... params) throws ResponseException {
        System.out.print("Are you sure you want to resign? (Y/N): ");
        Scanner scanner = new Scanner(System.in);
        while(true){
            String answer = scanner.nextLine().trim().toUpperCase();
            if(answer.equals("Y")){
                webSocketFacade.resign(authToken, activeGame.gameID());
                return "YOU LOST, HAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAHAH";
            }
            else if(answer.equals("N")){
                return "Resignation Cancelled, you remain in the game";
            }
            else {
                System.out.print("Invalid input, please type Y or N: ");
            }
        }

    }

    public String highlightMoves(String... params){
        int rowNum = getRowFromString(params[0]);
        int colNum = getColFromString(params[0]);

        ChessPosition position = new ChessPosition(rowNum, colNum);
        ChessBoard.highlightPieceMoves(activeGame.game(), position, isWhite);
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
                Make a move: "move" <Starting Square> <Ending Square>
                Resign but remain in the game: "resign"
                Highlight possible moves of an individual piece: "highlight" <Square where piece is located>
                """;
    }

    public static int getRowFromString(String square){
        return Character.getNumericValue(square.charAt(1));
    }

    public static int getColFromString(String square){
        return square.charAt(0) - 'a' + 1;
    }
}
