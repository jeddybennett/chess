package dataaccess;
import chess.ChessGame;
import model.GameData;

import java.util.Collection;
import java.util.HashMap;


public class MemoryGameDAO implements GameDAO{
    private int gameID;
    final private HashMap<Integer, GameData> games = new HashMap<>();

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return games.values();
    }

    @Override
    public void createGame(GameData gameData) throws DataAccessException {
        games.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
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
