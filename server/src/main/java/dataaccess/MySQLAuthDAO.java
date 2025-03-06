package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;

public class MySQLAuthDAO implements AuthDAO{

    public MySQLAuthDAO() throws ResponseException, DataAccessException {
        congfigureDatabase();
    }

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

    private void congfigureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            for(var statement : createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to configure database: %s" e.getMessage()));
        }
    }
}
