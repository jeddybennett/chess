package dataaccess;

import exception.ResponseException;
import model.GameData;

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

    }

    private void congfigureDatabase() throws ResponseException {

    }
}
