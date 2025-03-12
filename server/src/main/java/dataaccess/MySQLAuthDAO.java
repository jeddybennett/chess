package dataaccess;

import exception.ResponseException;
import model.AuthData;

import java.sql.SQLException;

import static java.sql.Types.NULL;

public class MySQLAuthDAO implements AuthDAO{

    public MySQLAuthDAO() throws ResponseException, DataAccessException {
        configureDatabase();
    }

    public void createAuth(AuthData authData) throws DataAccessException, ResponseException, SQLException {
            String authToken = authData.authToken();
            String username = authData.username();
            String statement = "INSERT into authData (authToken, username) VALUES (?, ?)";
            executeUpdate(statement, authToken, username);
    }

    public AuthData getAuth(String authToken) throws DataAccessException, SQLException, ResponseException {
        try(var conn = DatabaseManager.getConnection()){
            var Statement = "SELECT authToken, username FROM authData WHERE authToken = ?";
            try(var user = conn.prepareStatement(Statement)){
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
        String Statement = "DELETE from authData where authToken = ?";
        executeUpdate(Statement);
    }

    public void clearAuth() throws DataAccessException, ResponseException, SQLException {
        String Statement = "TRUNCATE authData";
        executeUpdate(Statement);
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

    private void configureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            for(var statement : createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", e.getMessage()));
        }
    }

    private void executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> preparedStatement.setString(i + 1, p);
                        case Integer p -> preparedStatement.setInt(i + 1, p);
                        case null -> preparedStatement.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
            }
        }
    }
}

