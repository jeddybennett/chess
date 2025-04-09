package websocket;

import org.eclipse.jetty.websocket.api.Session;
import spark.serialization.Serializer;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;

public class websocketRequestHandler {

    public void onMessage(Session session, String msg){
        try{
            UserGameCommand command = Serializer.fromJson(message, UserGameCommand.class);
            String username = getUsername(command.getAuthToken());

            saveSession(command.getGameID());

            switch(command.getCommandType()){
                case CONNECT -> connect(session, username, (ConnectCommand) command);
                case MAKE_MOVE -> makeMove(session, username, (MakeMoveCommand) command);
                case LEAVE -> leaveGame(session, username, (LeaveGameCommand) command);
                case RESIGN -> resign(session, username, (ResignCommand) command);

            }
        }
        catch (UnauthorizedException exception){
            sendMessage(session.getRemote(), new ErrorMessage("Error: unauthorized"));
        }
        catch(Exception ex){
            ex.printStackTrace();
            sendMessage(session.getRemote(), new ErrorMessage("Error: " + ex.getMessage()));
        }
    }
}
