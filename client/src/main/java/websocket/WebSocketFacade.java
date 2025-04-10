package websocket;

import chess.ChessMove;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;
import javax.websocket.*;
import java.net.URI;

public class WebSocketFacade extends Endpoint{

    Session session;
    ServerMessageObserver observer;

    public WebSocketFacade(String url, ServerMessageObserver observer) throws ResponseException {
        try{
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.observer = observer;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message){
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    observer.notify(serverMessage);
                }
            });
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void leave(String authToken, int gameID) throws ResponseException {
        WebSocketCommunicator.leave(authToken, gameID);
    }

    public void connect(String authToken, int gameID) throws ResponseException {
        WebSocketCommunicator.connect(authToken, gameID);
    }

    public void makeMove(String authToken, int gameID, ChessMove chessMove) throws ResponseException {
        WebSocketCommunicator.makeMove(authToken, gameID, chessMove);
    }

    public void resign(String authToken, int gameID) throws ResponseException{
        WebSocketCommunicator.resign(authToken, gameID);
    }
}
