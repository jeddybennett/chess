package dataaccess;
import java.util.Collection;

import model.GameData;


public interface GameDAO {

    Collection<GameData> listGames() throws DataAccessException;
    int createGame(String gameName) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(int old_gameID, GameData gameData) throws DataAccessException;
    void clearGame() throws DataAccessException;
}
