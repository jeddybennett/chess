package dataaccess;
import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;


public class MemoryGameDAO implements GameDAO{
    private int gameID = 0;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    public GameData createGame(String gameName) throws DataAccessException {
        gameID++;
        GameData gameData = new GameData(gameID, null, null, gameName, new ChessGame());
        games.put(gameID, gameData);
        return gameData;
    }

    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    public void updateGame(int old_gameID, GameData gameData) throws DataAccessException {
        games.replace(old_gameID, gameData);
    }

    public void clearGame(){
        games.clear();
    }

    public void deleteGame(int gameID){
        games.remove(gameID);
    }

}
