package dataaccess;

import exception.ResponseException;
import model.AuthData;

public class MySQLAuthDAO implements AuthDAO{

    public void createAuth(AuthData authData) throws DataAccessException {

    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public void clearAuth() throws DataAccessException {

    }

    private final String[] createStatements = {

    }

    private void congfigureDatabase() throws ResponseException {

    }
}
