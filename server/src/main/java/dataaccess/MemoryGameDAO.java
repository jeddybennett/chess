package dataaccess;
import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;


public class MemoryGameDAO implements GameDAO{
    private int gameID = 0;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public int createGame(String gameName) throws DataAccessException {

        gameID += 1;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, gameData);


        return gameID;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public void updateGame(int old_gameID, GameData gameData) throws DataAccessException {
        games.replace(old_gameID, gameData);
    }

    @Override
    public void clearGame(){
        games.clear();
    }

}
