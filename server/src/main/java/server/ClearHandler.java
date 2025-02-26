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

    public Object clearHandler(Request Req, Response Res) throws ResponseException, DataAccessException{
        try{
            clearService.clear();
            return new Gson().toJson(null);
        }
        catch(ResponseException exception){
            int error_code = exception.StatusCode();
            Res.status(error_code);
            throw exception;
        }
    }
}
