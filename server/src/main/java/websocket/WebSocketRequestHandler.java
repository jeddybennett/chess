package websocket;

import chess.*;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import com.google.gson.Gson;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;

import java.io.IOException;

public class WebSocketRequestHandler {
    private final ConnectionManager connectionManager;
    private final Gson gson = new Gson();
    private final UserService userService;
    private final GameService gameService;

    public WebSocketRequestHandler(ConnectionManager connectionManager, UserService userService, GameService gameService) {
        this.connectionManager = connectionManager;
        this.userService = userService;
        this.gameService = gameService;
    }

    public void onMessage(Session session, String msg){
        try{
            UserGameCommand command = gson.fromJson(msg, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());

            //saveSession(command.getGameID());

            switch(command.getCommandType()){
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);
                default -> sendMessage(session, "Error: " + command.getCommandType().toString() + " ");
            }
        }
        catch (Exception exception){
            sendMessage(session, "Error: Unauthorized");
        }
    }

    private void connect(Session session, String username, UserGameCommand command){
        try {
            connectionManager.add(username, session);
            GameData gameData = gameService.getGame(command.getGameID());
            String message;
            if (gameData.whiteUsername().equals(username)) {
                message = username + " " + "has joined as WHITE";
            } else if (gameData.blackUsername().equals(username)) {
                message = username + " " + "has joined as BLACK";
            } else {
                message = username + " " + "is observing the game";
            }
//            LoadGameMessage gameMessage = new LoadGameMessage(gameData);
//            connectionManager.broadcastGame(gameMessage);
            NotificationMessage notificationMessage = new NotificationMessage(message);
            connectionManager.broadcastString(username, notificationMessage);
        } catch (Exception e) {
            sendMessage(session, "Error: " + e.getMessage());
        }

    }

    private void makeMove(Session session, String username, MakeMoveCommand command){
        try {
            GameData gameData = gameService.getGame(command.getGameID());
            ChessMove chessMove = command.getMove();
            ChessGame chessGame = gameData.game();
            chessGame.makeMove(chessMove);

            ChessPosition startPosition = chessMove.getStartPosition();
            ChessPosition endPosition = chessMove.getEndPosition();
            ChessBoard chessBoard = chessGame.getBoard();
            ChessPiece chessPiece = chessBoard.getPiece(startPosition);

            GameData updatedGame = new GameData(gameData.gameID(), gameData.blackUsername(),
                    gameData.whiteUsername(), gameData.gameName(), chessGame);
            gameService.updateGame(command.getGameID(), updatedGame);

            LoadGameMessage gameMessage = new LoadGameMessage(updatedGame);
            connectionManager.broadcastGame(gameMessage);

            NotificationMessage notificationMessage = new NotificationMessage(username + " has moved "
                    + chessPiece.getPieceType().toString() + " to: " + endPosition.toString());
            connectionManager.broadcastString(username, notificationMessage);
        } catch (Exception e) {
            sendMessage(session, "Error: " + e.getMessage());
        }
    }

    private void leaveGame(Session session, String username, UserGameCommand command){
        try {
            connectionManager.remove(username);
            NotificationMessage notificationMessage = new NotificationMessage(username + " "
                    + "has left the game");
            GameData currentGame = gameService.getGame(command.getGameID());
            GameData updatedGame;
            if(currentGame.whiteUsername().equals(username)){
                updatedGame = new GameData(currentGame.gameID(), null, currentGame.blackUsername(),
                        currentGame.gameName(), currentGame.game());
            }
            else{
                updatedGame = new GameData(currentGame.gameID(), currentGame.whiteUsername(), null,
                        currentGame.gameName(), currentGame.game());
            }
            gameService.updateGame(command.getGameID(), updatedGame);
            connectionManager.broadcastString(username, notificationMessage);
        } catch (Exception e) {
            sendMessage(session, "Error: " + e.getMessage());
        }
    }

    private void resign(Session session, String username, UserGameCommand command){
        try {
            int gameID = command.getGameID();
            GameData gameData = gameService.getGame(gameID);
            gameData.game().setTeamTurn(null);
            gameService.updateGame(gameID, gameData);
            NotificationMessage message = new NotificationMessage(username + " has resigned from the game");
            connectionManager.broadcastString(null, message);
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
}
