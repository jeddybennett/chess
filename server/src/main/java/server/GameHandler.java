package server;

import com.google.gson.JsonObject;
import service.GameService;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.*;
import spark.Request;
import spark.Response;
public class GameHandler {
    private final GameService gameService;

    public GameHandler(GameService gameService) {
        this.gameService = gameService;
    }

    public Object creatGameHandler(Request Req, Response Res) throws ResponseException, DataAccessException {
        JsonObject jsonObject = new Gson().fromJson(Req.body(), JsonObject.class);
        jsonObject.addProperty("authToken", Req.headers("authorization"));
        CreateGameRequest createGameRequest = new Gson().fromJson(jsonObject, CreateGameRequest.class);
        try{
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            return new Gson().toJson(createGameResult);
        }
        catch(ResponseException exception){
            throw throw_error(exception, Res);
        }

    }

    public Object joinGameHandler(Request Req, Response Res) throws ResponseException, DataAccessException{
        JsonObject jsonObject = new Gson().fromJson(Req.body(), JsonObject.class);
        jsonObject.addProperty("authToken", Req.headers("authorization"));
        try{
            JoinGameRequest joinGameRequest = new Gson().fromJson(jsonObject, JoinGameRequest.class);
            gameService.joinGame(joinGameRequest);
            return new Gson().toJson(null);
        }
        catch (ResponseException exception){
            throw throw_error(exception, Res);
        }
    }

    public Object listGameHandler(Request Req, Response Res) throws ResponseException, DataAccessException{
        try{
            ListGameRequest listGameRequest = new ListGameRequest(Req.headers("authorization"));
            ListGameResult listGameResult = gameService.ListGames(listGameRequest);
            return new Gson().toJson(listGameResult);
        }
        catch (ResponseException exception){
            throw throw_error(exception, Res);

        }
    }
    public ResponseException throw_error(ResponseException exception, Response Res){
        int error_code = exception.StatusCode();
        Res.status(error_code);
        return exception;
    }

}
