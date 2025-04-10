package websocket;

import chess.*;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.GameData;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import spark.serialization.Serializer;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import com.google.gson.Gson;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.sql.SQLException;

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
                case MAKE_MOVE -> makeMove(username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(username);
                case RESIGN -> resign(username, command);

            }
        }
        catch (Exception exception){
            sendMessage(session, new ErrorMessage("Error: unauthorized"));
        }
        catch(Exception ex){
            //ex.printStackTrace();
            sendMessage(session, new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, UserGameCommand command) throws SQLException, ResponseException, DataAccessException, IOException {
        connectionManager.add(username, session);
        GameData gameData = gameService.getGame(command.getGameID());
        String message;
        if(gameData.whiteUsername().equals(username)){
            message = username + " " + "has joined as WHITE";
        }
        else if (gameData.blackUsername().equals(username)) {
            message = username + " " + "has joined as BLACK";
        }
        else{
            message = username + " " + "is observing the game";
        }
        LoadGameMessage gameMessage = new LoadGameMessage(gameData);
        sendMessage(session, gameMessage);
        connectionManager.broadcastGame(gameMessage);
        NotificationMessage notificationMessage = new NotificationMessage(message);
        connectionManager.broadcastString(username, gson.toJson(notificationMessage));


    }

    private void makeMove(String username, MakeMoveCommand command) throws SQLException, ResponseException,
            DataAccessException, InvalidMoveException, IOException {
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

        String notificationMessage = new NotificationMessage(username + " has moved "
                +chessPiece.getPieceType().toString() + " to: " + endPosition.toString()).getMessage();
        connectionManager.broadcastString(username, notificationMessage);
    }

    private void leaveGame(String username) throws IOException {
        connectionManager.remove(username);
        String notificationMessage = new NotificationMessage(username + " "
        + "has left the game").getMessage();
        connectionManager.broadcastString(username, gson.toJson(notificationMessage));
    }

    private void resign(String username, UserGameCommand command) throws SQLException, ResponseException,
            DataAccessException, IOException {
        int gameID = command.getGameID();
        GameData gameData = gameService.getGame(gameID);
        gameData.game().setTeamTurn(null);
        gameService.updateGame(gameID, gameData);
        String message = username + " has resigned from the game";
        connectionManager.broadcastString(null, message);
    }

    private String getUsername(String authToken) throws ResponseException {
        return userService.getUsername(authToken);
    }

    private void sendMessage(Session session, ServerMessage message) {
        try {
            if (session.isOpen()) {
                String json = new Gson().toJson(message);
                session.getRemote().sendString(json);
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
