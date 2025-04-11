package websocket;

import chess.*;
import com.google.gson.JsonObject;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import com.google.gson.Gson;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;
import java.nio.channels.MembershipKey;

@WebSocket
public class WebSocketRequestHandler {
    private final ConnectionManager connectionManager = new ConnectionManager();
    private final Gson gson = new Gson();
    private final UserService userService;
    private final GameService gameService;
    private Boolean didResign = false;

    public WebSocketRequestHandler(UserService userService, GameService gameService) {
        this.userService = userService;
        this.gameService = gameService;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String msg){
        try{
            JsonObject json = gson.fromJson(msg, JsonObject.class);
            String type = json.get("commandType").getAsString();
            UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(type);
            switch(commandType){

                case CONNECT -> {
                    UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);
                    String username = getUsername(command.getAuthToken());
                    connect(session, username, command);
                }
                case MAKE_MOVE -> {
                    MakeMoveCommand command = gson.fromJson(msg, MakeMoveCommand.class);
                    String username = getUsername(command.getAuthToken());
                    makeMove(session, username, command);
                }
                case LEAVE -> {
                    UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);
                    String username = getUsername(command.getAuthToken());
                    leaveGame(session, username, command);
                }
                case RESIGN -> {
                    UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);
                    String username = getUsername(command.getAuthToken());
                    resign(session, username, command);
                }
                default -> {
                    UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);
                    sendMessage(session, "Error: " + command.getCommandType().toString() + " ");
                }
            }
        }
        catch (Exception exception){
            sendMessage(session, "Error: Unauthorized");
        }
    }

    private void connect(Session session, String username, UserGameCommand command){
        try {
            didResign = false;
            connectionManager.add(username, session, command.getGameID());
            GameData gameData = gameService.getGame(command.getGameID());
            String message;
            if (username.equals(gameData.whiteUsername())) {
                message = username + " " + "has joined as WHITE";
            } else if (username.equals(gameData.blackUsername())) {
                message = username + " " + "has joined as BLACK";
            } else {
                message = username + " " + "is observing the game";
            }
            LoadGameMessage gameMessage = new LoadGameMessage(gameData);
            connectionManager.broadcastUser(username, gameMessage);
            NotificationMessage notificationMessage = new NotificationMessage(message);
            connectionManager.broadcastString(command.getGameID() ,username, notificationMessage);
        } catch (Exception e) {
            sendMessage(session, "Error: " + e.getMessage());
        }

    }

    private void makeMove(Session session, String username, MakeMoveCommand command) {
        try {
            if (didResign) {
                sendMessage(session, "Error: no more moves can be made after a resignation");
                return;
            }
            GameData gameData = gameService.getGame(command.getGameID());
            ChessMove chessMove = command.getMove();
            ChessGame chessGame = gameData.game();
            ChessGame.TeamColor correctTurn = chessGame.getTeamTurn();
            ChessGame.TeamColor playerColor = null;
            String opponentUsername = null;
            if (username.equals(gameData.whiteUsername())) {
                playerColor = ChessGame.TeamColor.WHITE;
                opponentUsername = gameData.blackUsername();
            } else if (username.equals(gameData.blackUsername())) {
                playerColor = ChessGame.TeamColor.BLACK;
                opponentUsername = gameData.whiteUsername();
            }
            if (!correctTurn.equals(playerColor)) {
                sendMessage(session, "Error: not your turn");
                return;
            }
            ChessGame.TeamColor opponentColor = (correctTurn == ChessGame.TeamColor.WHITE)
                    ? ChessGame.TeamColor.BLACK : ChessGame.TeamColor.WHITE;
            ChessPosition startPosition = chessMove.getStartPosition();
            ChessPosition endPosition = chessMove.getEndPosition();
            ChessBoard chessBoard = chessGame.getBoard();
            ChessPiece chessPiece = chessBoard.getPiece(startPosition);

            chessGame.makeMove(chessMove);
            GameData updatedGame = new GameData(gameData.gameID(),
                    gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), chessGame);

            boolean inCheckmate = chessGame.isInCheckmate(opponentColor);
            boolean inStalemate = chessGame.isInStalemate(opponentColor);
            boolean inCheck = chessGame.isInCheck(opponentColor);
            NotificationMessage notificationMessage;
            if (inCheckmate) {
                notificationMessage = new NotificationMessage(opponentUsername + " is in Checkmate. THEY LOSE!!!");
                connectionManager.broadcastString(command.getGameID(), null, notificationMessage);
            } else if (inStalemate) {
                notificationMessage = new NotificationMessage("Stalemate. It's a draw");
                connectionManager.broadcastString(command.getGameID(), null, notificationMessage);
            } else if (inCheck) {
                notificationMessage = new NotificationMessage(opponentUsername + " Is in Check! Protect the King");
                connectionManager.broadcastString(command.getGameID(), null, notificationMessage);
            }
            String chessPositionString = toChessNotation(endPosition);
            notificationMessage = new NotificationMessage(username + " has moved "
                    + chessPiece.getPieceType().toString() + " to: " + chessPositionString);
            connectionManager.broadcastString(command.getGameID(), username, notificationMessage);

            gameService.updateGame(command.getGameID(), updatedGame);
            LoadGameMessage gameMessage = new LoadGameMessage(updatedGame);
            connectionManager.broadcastGame(command.getGameID(), gameMessage);
        } catch (Exception e) {
            if (e.getMessage().contains("turn")) {
                sendMessage(session, "Error: not your turn");
            } else {
                sendMessage(session, "Error: invalid move, please type <starting square> <ending square>");
            }
        }
    }

    private void leaveGame(Session session, String username, UserGameCommand command){
        try {
            connectionManager.remove(username);
            NotificationMessage notificationMessage = new NotificationMessage(username + " "
                    + "has left the game");
            GameData currentGame = gameService.getGame(command.getGameID());
            GameData updatedGame;
            if(username.equals(currentGame.whiteUsername())){
                updatedGame = new GameData(currentGame.gameID(), null, currentGame.blackUsername(),
                        currentGame.gameName(), currentGame.game());
                gameService.updateGame(command.getGameID(), updatedGame);
            }
            else if(username.equals(currentGame.blackUsername())){
                updatedGame = new GameData(currentGame.gameID(), currentGame.whiteUsername(), null,
                        currentGame.gameName(), currentGame.game());
                gameService.updateGame(command.getGameID(), updatedGame);
            }
            connectionManager.broadcastString(command.getGameID(),username, notificationMessage);
        } catch (Exception e) {
            sendMessage(session, "Error: " + e.getMessage());
        }
    }

    private void resign(Session session, String username, UserGameCommand command){
        try {
            if(didResign){
                sendMessage(session, "Error: player has already resigned");
                return;
            }
            GameData currentGame = gameService.getGame(command.getGameID());
            if(!username.equals(currentGame.blackUsername()) && !username.equals(currentGame.whiteUsername())){
                sendMessage(session, "Error: Observer cannot resign");
                return;
            }
            didResign = true;
            NotificationMessage message = new NotificationMessage(username + " has resigned from the game");
            connectionManager.broadcastString(command.getGameID(),null, message);
        } catch (Exception e) {
            sendMessage(session, "Error: " + e.getMessage());
        }
    }

    private String getUsername(String authToken) throws ResponseException {
        return userService.getUsername(authToken);
    }

    private void sendMessage(Session session, String message) {
        try {
            if (session.isOpen()) {
                ErrorMessage error = new ErrorMessage(message);
                session.getRemote().sendString(gson.toJson(error));
            }
            else{
                System.err.println("Error: Session is not open");
            }
        }
        catch(IOException e){
            System.err.println("Error sending message: " + e.getMessage());
        }
    }

    private static String toChessNotation(ChessPosition position) {
        int row = position.getRow();
        int col = position.getColumn();

        char file = (char) ('a' + col - 1);

        return file + Integer.toString(row);
    }
}
