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
        try{
            CreateGameRequest createGameRequest = new CreateGameRequest(Req.headers("authorization"), Req.body());
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            return new Gson().toJson(createGameResult);
        }
        catch(ResponseException exception){
            throw throw_error(exception, Res);
        }

    }

    public Object joinGameHandler(Request Req, Response Res) throws ResponseException, DataAccessException{
        try{
            JoinGameRequest joinGameRequest = new JoinGameRequest(Req.headers("authorization"), Req.body());
            gameService.joinGame(joinGameRequest);
            return new Gson().toJson(null);
        }
        catch (ResponseException exception){
            throw throw_error(exception, Res);
        }
    }

    public Object listGameHandler(Request Req, Response Res) throws ResponseException, DataAccessException{
        try{
            
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
