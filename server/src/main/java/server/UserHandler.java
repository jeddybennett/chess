package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import exception.ResponseException;
import model.*;
import service.UserService;
import spark.Request;
import spark.Response;

public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    public Object registerHandler(Request req, Response Res) throws ResponseException, DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        try{
            RegisterResult registerResult = userService.register(registerRequest);
            return new Gson().toJson(registerResult);
        }
        catch(ResponseException exception){
            throw throw_error(exception, Res);
        }
    }

    public Object loginHandler(Request req, Response Res) throws ResponseException, DataAccessException{
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try{
            LoginResult loginResult = userService.login(loginRequest);
            return new Gson().toJson(loginResult);
        }
        catch(ResponseException exception){
            throw throw_error(exception, Res);
        }
    }

    public Object logoutHandler(Request req, Response Res) throws ResponseException, DataAccessException{
        LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
        try{
            userService.logout(logoutRequest);
            return new Gson().toJson(null);
        }
        catch(ResponseException exception){
            throw throw_error(exception, Res);
        }
    }

    public ResponseException throw_error(ResponseException exception, Response Res){
        int error_code = exception.StatusCode();
        Res.status(error_code);
        return exception;
    }
}
