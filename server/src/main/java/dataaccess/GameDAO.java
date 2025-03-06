package dataaccess;
import java.sql.SQLException;
import java.util.Collection;

import exception.ResponseException;
import model.GameData;


public interface GameDAO {

    Collection<GameData> listGames() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException, ResponseException;
    GameData getGame(int gameID) throws DataAccessException, SQLException, ResponseException;
    void updateGame(int oldGameID, GameData gameData) throws DataAccessException;
    void clearGame() throws DataAccessException;
}
