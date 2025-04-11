package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;

public class WebSocketCommunicator{

    private final Session session;

    public WebSocketCommunicator(Session session) {
        this.session = session;
    }


    public void leave(String authToken, int gameID) throws ResponseException {
        try {
            var leaveCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            String message = new Gson().toJson(leaveCommand);
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }


    public void connect(String authToken, int gameID) throws ResponseException{
        try{
            var connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            String message = new Gson().toJson(connectCommand);
            this.session.getBasicRemote().sendText(message);
        }
        catch (Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove chessMove) throws ResponseException{
        try{
            var makeMoveCommand = new MakeMoveCommand(authToken, gameID, chessMove);
            String message = new Gson().toJson(makeMoveCommand);
            this.session.getBasicRemote().sendText(message);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException{
        try {
            var resignCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            String message = new Gson().toJson(resignCommand);
            this.session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }


}
