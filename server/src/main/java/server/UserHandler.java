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

    public Object registerHandler(Request req, Response res) throws ResponseException, DataAccessException {
        RegisterRequest registerRequest = new Gson().fromJson(req.body(), RegisterRequest.class);
        try{
            RegisterResult registerResult = userService.register(registerRequest);
            return new Gson().toJson(registerResult);
        }
        catch(ResponseException exception){
            throw throwError(exception, res);
        }
    }

    public Object loginHandler(Request req, Response res) throws ResponseException, DataAccessException{
        LoginRequest loginRequest = new Gson().fromJson(req.body(), LoginRequest.class);
        try{
            LoginResult loginResult = userService.login(loginRequest);
            return new Gson().toJson(loginResult);
        }
        catch(ResponseException exception){
            throw throwError(exception, res);
        }
    }

    public Object logoutHandler(Request req, Response res) throws ResponseException, DataAccessException{
        LogoutRequest logoutRequest = new LogoutRequest(req.headers("authorization"));
        try{
            userService.logout(logoutRequest);
            return new Gson().toJson(null);
        }
        catch(ResponseException exception){
            throw throwError(exception, res);
        }
    }

    public ResponseException throwError(ResponseException exception, Response res){
        int errorCode = exception.statusCode();
        res.status(errorCode);
        return exception;
    }
}
