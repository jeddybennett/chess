package dataaccess;
import java.sql.SQLException;
import java.util.Collection;

import exception.ResponseException;
import model.GameData;


public interface GameDAO {

    Collection<GameData> listGames() throws DataAccessException, SQLException, ResponseException;
    int createGame(String gameName) throws DataAccessException, ResponseException, SQLException;
    GameData getGame(int gameID) throws DataAccessException, SQLException, ResponseException;
    void updateGame(int oldGameID, GameData gameData) throws DataAccessException, SQLException, ResponseException;
    void clearGame() throws DataAccessException, SQLException, ResponseException;
}
