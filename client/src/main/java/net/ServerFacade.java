package net;

import model.*;
import net.ClientCommunicator.*;
import ui.Client;

public class ServerFacade {

    private final String serverURL;
    public ServerFacade(String serverURL) {
        this.serverURL = serverURL;
    }

    //should have 7 methods that talk to ClientCommunicator for an HTTP call
    public RegisterResult register(RegisterRequest registerRequest){
        return null;
    }

    public LoginResult login(LoginRequest loginRequest){
        return null;
    }

    public void join(JoinGameRequest joinGameRequest){
    }

    public void clear(ClearRequest clearRequest){
    }

    public ListGameResult listGames(ListGameRequest listGameRequest){
        String authToken = listGameRequest.authToken();
//      ClientCommunicator.getlistGames();
        return null;
    }




}
