package dataaccess;

import exception.ResponseException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySQLGameDAO implements GameDAO{

    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    public void updateGame(int oldGameID, GameData gameData) throws DataAccessException {

    }

    public void clearGame() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameData(
            'gameID' int NOT NULL AUTO_INCREMENT
            'whiteUsername' varchar(256) DEFAULT NULL
            'blackUsername' varchar(256) DEFAULT NULL
            'gameName' varchar(256) NOT NULL
            'chessGame' JSON NOT NULL
            PRIMARY KEY('gameID')
            )
            """

    };

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
            throw new ResponseException(500, "Unable to configure database: " + e.getMessage());
        }
    }
}
