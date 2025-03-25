package net;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;
import exception.ResponseException;
import model.*;
import net.ClientCommunicator;
import ui.Client;

public class ServerFacade {

    private final ClientCommunicator clientCommunicator;
    public ServerFacade(String url) {
        this.clientCommunicator = new ClientCommunicator(url);
    }

    //should have 7 methods that talk to ClientCommunicator for an HTTP call
    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {
        var path = "/user";
        var method = "POST";
        return clientCommunicator.makeRequest(method, path, registerRequest, RegisterResult.class, null);
    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {
        var path = "/session";
        var method = "POST";
        return clientCommunicator.makeRequest(method, path, loginRequest, LoginResult.class, null);
    }

    public void logout(String authToken) throws ResponseException{
        var path = "/session";
        var method = "DELETE";
        clientCommunicator.makeRequest(method, path, null, null, authToken);
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException{
        String authToken = joinGameRequest.authToken();
        int gameID = joinGameRequest.gameID();
        ChessGame.TeamColor teamColor = joinGameRequest.playerColor();
        var path = "/game";
        var method = "PUT";
        clientCommunicator.makeRequest(method, path, joinGameRequest, null, authToken);
    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException{
        String authToken = createGameRequest.authToken();
        var path = "/game";
        var method = "POST";
        return clientCommunicator.makeRequest(method, path, createGameRequest, CreateGameResult.class, authToken);
    }

    public ListGameResult listGames(ListGameRequest listGameRequest) throws ResponseException{
        String authToken = listGameRequest.authToken();
        System.out.println(authToken);
        var path = "/game";
        var method = "GET";
        return clientCommunicator.makeRequest(method, path, null, ListGameResult.class, authToken);
    }

    public void clear(ClearRequest clearRequest) throws ResponseException {
        String authToken = clearRequest.authToken();
        int gameID = clearRequest.gameID();
        var path = "/db";
        var method = "DELETE";
        clientCommunicator.makeRequest(method, path, clearRequest, null, authToken);
    }






}
