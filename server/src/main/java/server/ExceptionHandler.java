package server;

import exception.ResponseException;
import spark.Request;
import spark.Response;

public class ExceptionHandler {

    public void exceptionHandler(ResponseException exception, Request req, Response res){
        int errorCode = exception.statusCode();
        res.status(errorCode);
        res.body(exception.toJson());
    }
}
