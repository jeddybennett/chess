package websocket;

import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;

public class WebSocketCommunicator {

    public void onMessage(String message){
        try{
            ServerMessage message = gson.fromJson(message, ServerMessage.class);
            observer.notify(message);
        }
        catch(Exception ex){
            observer.notify(new ErrorMessage(ex.getMessage()));
        }
    }
}
