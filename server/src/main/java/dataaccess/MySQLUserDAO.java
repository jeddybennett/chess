package dataaccess;

import exception.ResponseException;
import model.UserData;

public class MySQLUserDAO implements UserDAO{


    public UserData getUser(String userName) throws DataAccessException {
        return null;
    }

    public void createUser(UserData u) throws DataAccessException {

    }

    public void clearUser() throws DataAccessException {

    }

    private final String[] createStatements = {

    }

    private void congfigureDatabase() throws ResponseException {

    }
}
