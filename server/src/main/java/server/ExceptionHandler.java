package server;

import exception.ResponseException;
import spark.Request;
import spark.Response;

public class ExceptionHandler {

    public void exceptionHandler(ResponseException exception, Request Req, Response Res){
        int error_code = exception.StatusCode();
        Res.status(error_code);
        Res.body(exception.toJson());
    }
}
