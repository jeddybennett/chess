package server;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.RegisterRequest;
import service.*;
import exception.*;
import spark.*;

public class ClearHandler {

    private final ClearService clearService;

    public ClearHandler(ClearService clearService) {
        this.clearService = clearService;
    }

    public Object clearHandler(Request req, Response res) throws ResponseException{
        try{
            clearService.clear();
            return new Gson().toJson(null);
        }
        catch(ResponseException exception){
            int errorCode = exception.statusCode();
            res.status(errorCode);
            throw exception;
        }
    }
}
