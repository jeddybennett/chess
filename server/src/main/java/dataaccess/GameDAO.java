package dataaccess;
import java.util.Collection;

import model.GameData;


public interface GameDAO {

    Collection<GameData> listGames() throws DataAccessException;
    void createGame(GameData gameData) throws DataAccessException;
    GameData getGame(int gameID) throws DataAccessException;
    void updateGame(int old_gameID, GameData gameData) throws DataAccessException;

}
