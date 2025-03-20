package net;

import model.*;
import net.ClientCommunicator.*;
import ui.Client;

public class ServerFacade {

    //should have 7 methods that talk to ClientCommunicator for an HTTP call
    public RegisterResult register(RegisterRequest registerRequest){

    }

    public LoginResult login(LoginRequest loginRequest){

    }

    public void join(JoinGameRequest joinGameRequest){

    }

    public void clear(ClearRequest clearRequest){

    }

    public ListGameResult listGames(ListGameRequest listGameRequest){
        String authToken = listGameRequest.authToken();
        ClientCommunicator.getlistGames();
    }




}
