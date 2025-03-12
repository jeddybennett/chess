package server;

import com.google.gson.JsonObject;
import service.GameService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.*;
import spark.Request;
import spark.Response;

import java.sql.SQLException;

public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object creatGameHandler(Request req, Response res) throws ResponseException, SQLException{
        JsonObject jsonObject = new Gson().fromJson(req.body(), JsonObject.class);
        jsonObject.addProperty("authToken", req.headers("authorization"));
        CreateGameRequest createGameRequest = new Gson().fromJson(jsonObject, CreateGameRequest.class);
        try{
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            return new Gson().toJson(createGameResult);
        }
        catch(ResponseException exception){
            throw throwError(exception, res);
        }

    }

    public Object joinGameHandler(Request req, Response res) throws ResponseException, SQLException{
        JsonObject jsonObject = new Gson().fromJson(req.body(), JsonObject.class);
        jsonObject.addProperty("authToken", req.headers("authorization"));
        try{
            JoinGameRequest joinGameRequest = new Gson().fromJson(jsonObject, JoinGameRequest.class);
            gameService.joinGame(joinGameRequest);
            return new Gson().toJson(null);
        }
        catch (ResponseException exception){
            throw throwError(exception, res);
        }
    }

    public Object listGameHandler(Request req, Response res) throws ResponseException, DataAccessException, SQLException {
        try{
            ListGameRequest listGameRequest = new ListGameRequest(req.headers("authorization"));
            ListGameResult listGameResult = gameService.listGames(listGameRequest);
            return new Gson().toJson(listGameResult);
        }
        catch (ResponseException exception){
            throw throwError(exception, res);

        }
    }
    public ResponseException throwError(ResponseException exception, Response res){
        int errorCode = exception.statusCode();
        res.status(errorCode);
        return exception;
    }

}
