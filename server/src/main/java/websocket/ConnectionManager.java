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

    public void add(String username, Session session) {
        var connection = new Connection(username, session);
        connections.put(username, connection);
    }

    public void remove(String username) {
        connections.remove(username);
    }

    public void broadcastString(String excludeUsername, NotificationMessage notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
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

    public void broadcastUser(String username, String error) throws IOException, ResponseException {
        try{
            var connection = connections.get(username);
            connection.send(error);
        }
        catch(Exception e){
            throw new ResponseException(500, e.getMessage());
        }
    }

    public void broadcastGame(LoadGameMessage loadGameMessage) throws IOException {
        for(var c: connections.values()){
            if(c.session.isOpen()){
                try {
                    c.send(new Gson().toJson(loadGameMessage.getGame()));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                connections.remove(c.username);
            }
        }

    }
}
