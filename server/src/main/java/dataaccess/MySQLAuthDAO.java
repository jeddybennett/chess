package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;
import dataaccess.MySQLGameDAO.*;

import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO{

    public MySQLAuthDAO() throws ResponseException, DataAccessException {
        authConfigureDatabase();
    }

    public void createAuth(AuthData authData) throws DataAccessException, ResponseException, SQLException {
            String authToken = authData.authToken();
            String username = authData.username();
            String statement = "INSERT into authData (authToken, username) VALUES (?, ?)";
            authExecuteUpdate(statement, authToken, username);
    }

    public AuthData getAuth(String authToken) throws DataAccessException, SQLException, ResponseException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken, username FROM authData WHERE authToken = ?";
            try(var user = conn.prepareStatement(statement)){
                user.setString(1,  authToken);
                try(var returnedUser = user.executeQuery()){
                    if(returnedUser.next()){
                        String username = returnedUser.getString("username");
                        return new AuthData(authToken, username);
                    }
                }
            }
        }
        catch(SQLException exception){
            throw new ResponseException(500, String.format("Unable to configure database: %s", exception.getMessage()));
        }
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException, ResponseException, SQLException {
        String statement = "DELETE from authData where authToken = ?";
        authExecuteUpdate(statement, authToken);
    }

    public void clearAuth() throws DataAccessException, ResponseException, SQLException {
        String statement = "TRUNCATE authData";
        authExecuteUpdate(statement);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authData(
            authToken VARCHAR(256) NOT NULL,
            username VARCHAR(256) NOT NULL,
            PRIMARY KEY (authToken)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void authConfigureDatabase() throws ResponseException, DataAccessException {
        MySQLGameDAO.configureDatabase(createStatements);
    }

    private void authExecuteUpdate(String statement, Object... params) throws ResponseException, DataAccessException, SQLException {
        MySQLGameDAO.executeUpdate(statement, params);
    }
}

