package websocket;
import com.google.gson.Gson;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

public class ConnectionManager{
    public final ConcurrentHashMap<String, Connection> connections = new ConcurrentHashMap<>();

    public void add(String username, Session session, int gameID) {
        var connection = new Connection(username, session, gameID);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcastString(int gameID, String excludeUsername, NotificationMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername) && c.gameID == gameID) {
                    c.send(new Gson().toJson(notification));
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.username);
        }
    }

    public void broadcastUser(String username, LoadGameMessage loadGameMessage) throws ResponseException {
        try{
            var connection = connections.get(username);
            connection.send(new Gson().toJson(loadGameMessage));
        }
        catch(Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void broadcastGame(int gameID, LoadGameMessage loadGameMessage) throws IOException {
        for(var c: connections.values()){
            if(c.session.isOpen()){
                try {
                    // Send the complete LoadGameMessage object, not just the game
                    if(c.gameID == gameID){
                        c.send(new Gson().toJson(loadGameMessage));
                    }

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                connections.remove(c.username);
            }
        }
    }

}
