package websocket;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import websocket.messages.LoadGameMessage;
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

    public void broadcastString(String excludeUsername, String notification) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.username.equals(excludeUsername)) {
                    c.send(notification);
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

    public void broadcastGame(LoadGameMessage loadGameMessage) throws IOException {
        for(var c: connections.values()){
            if(c.session.isOpen()){
                try {
                    c.send(new Gson().toJson(loadGameMessage));
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
