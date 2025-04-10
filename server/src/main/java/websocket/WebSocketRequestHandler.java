package websocket;

import exception.ResponseException;
import org.eclipse.jetty.server.Authentication;
import org.eclipse.jetty.websocket.api.Session;
import service.GameService;
import service.UserService;
import spark.serialization.Serializer;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import com.google.gson.Gson;

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

            saveSession(command.getGameID());

            switch(command.getCommandType()){
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, command);
                case LEAVE -> leaveGame(session, username, command);
                case RESIGN -> resign(session, username, command);

            }
        }
        catch (Exception exception){
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        }
        catch(Exception ex){
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }

    private void connect(Session session, String username, UserGameCommand command){

    }

    private void makeMove(Session session, String username, UserGameCommand command){

    }

    private void leaveGame(Session session, String username, UserGameCommand command){

    }

    private void resign(Session session, String username, UserGameCommand command){

    }

    private String getUsername(String authToken) throws ResponseException {
        return UserService.getUsername(authToken);
    }

    private void
}
