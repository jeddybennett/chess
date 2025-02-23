package dataaccess;
import java.util.Collection;

import chess.ChessGame;
import model.GameData;


public interface GameDAO {

    Collection<GameData> listGames() throws DataAccessException;
    void createGame(GameData gameData) throws DataAccessException;
    ChessGame getGame(int gameID) throws DataAccessException;
    void updateGame(ChessGame newGame, GameData gameData) throws DataAccessException;


}
