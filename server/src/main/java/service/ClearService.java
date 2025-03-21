package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import exception.ResponseException;

import java.sql.SQLException;

public class ClearService {
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final UserDAO userDAO;


    public ClearService(GameDAO gameDAO, AuthDAO authDAO, UserDAO userDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        this.userDAO = userDAO;
    }

    public void clear() throws ResponseException {
        try{
            gameDAO.clearGame();
            authDAO.clearAuth();
            userDAO.clearUser();
        }
        catch(DataAccessException | SQLException exception){
            throw new ResponseException(500, "Error: " + exception.getMessage());
        }
    }
}
