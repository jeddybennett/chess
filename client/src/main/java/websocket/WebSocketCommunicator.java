package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;

public class WebSocketCommunicator {

    static Session session;

    public static void leave(String authToken, int gameID) throws ResponseException {
        try {
            var leaveCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            String message = new Gson().toJson(leaveCommand);
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public static void connect(String authToken, int gameID) throws ResponseException{
        try{
            var connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
            String message = new Gson().toJson(connectCommand);
            session.getBasicRemote().sendText(message);
        }
        catch (Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    public static void makeMove(String authToken, int gameID, ChessMove chessMove) throws ResponseException{
        try{
            var makeMoveCommand = new MakeMoveCommand(authToken, gameID, chessMove);
            String message = new Gson().toJson(makeMoveCommand);
            session.getBasicRemote().sendText(message);
        }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    public static void resign(String authToken, int gameID) throws ResponseException{
        try {
            var resignCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            String message = new Gson().toJson(resignCommand);
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }


}
