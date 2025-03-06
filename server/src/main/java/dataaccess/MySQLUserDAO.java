package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO{

    public MySQLUserDAO() throws ResponseException, DataAccessException {
        congfigureDatabase();
    }


    public UserData getUser(String userName) throws DataAccessException {
        return null;
    }

    public void createUser(UserData u) throws DataAccessException {

    }

    public void clearUser() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userData(
                'username' varchar(256) NOT NULL
                'email' varchar(256) NOT NULL
                'password' varchar(256) NOT NULL
                PRIMARY KEY ('username')
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
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
