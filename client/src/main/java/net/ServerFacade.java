package net;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import model.*;
import net.ClientCommunicator;
import ui.Client;

public class ServerFacade {

    private final String serverURL;
    public ServerFacade(String url) {
        serverURL = url;
    }

    //should have 7 methods that talk to ClientCommunicator for an HTTP call
    public RegisterResult register(RegisterRequest registerRequest){
        String email = registerRequest.email();
        String password = registerRequest.password();
        String username = registerRequest.username();
        var path = "/user";
        var method = "POST";
        return ClientCommunicator.makeRequest(method, path, registerRequest, RegisterResult.class);
    }

    public LoginResult login(LoginRequest loginRequest){
        String username = loginRequest.username();
        String password = loginRequest.password();
        var path = "/session";
        var method = "POST";
        return ClientCommunicator.makeRequest(method, path, loginRequest, LoginResult.class);
    }

    public void logout(LogoutRequest logoutRequest){
        String authToken = logoutRequest.authToken();
        var path = "/session";
        var method = "DELETE";
        ClientCommunicator.makeRequest(method, path, logoutRequest, null);
    }

    public void joinGame(JoinGameRequest joinGameRequest){
        String authToken = joinGameRequest.authToken();
        int gameID = joinGameRequest.gameID();
        ChessGame.TeamColor teamColor = joinGameRequest.playerColor();
        var path = "/game";
        var method = "PUT";
        ClientCommunicator.makeRequest(method, path, joinGameRequest, null);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest){
        String authToken = createGameRequest.authToken();
        String gameName = createGameRequest.gameName();
        var path = "/game";
        var method = "POST";
        return ClientCommunicator.makeRequest(method, path, createGameRequest, CreateGameResult.class);
    }

    public ListGameRequest listGames(ListGameRequest listGameRequest){
        String authToken = listGameRequest.authToken();
        var path = "/game";
        var method = "GET";
        return ClientCommunicator.makeRequest(method, path, listGameRequest, ListGameResult.class);
    }

    public void clear(ClearRequest clearRequest){
        String authToken = clearRequest.authToken();
        int gameID = clearRequest.gameID();
        var path = "/db";
        var method = "DELETE";
        ClientCommunicator.makeRequest(method, path, clearRequest, null);
    }






}
