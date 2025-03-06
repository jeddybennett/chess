package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

public class MySQLGameDAO implements GameDAO{

    public MySQLGameDAO() throws ResponseException, DataAccessException{
        congfigureDatabase();
    }

    public Collection<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    public int createGame(String gameName) throws DataAccessException, ResponseException {
        int gameID = getGameID();
        ChessGame chessGame = new ChessGame();
        Gson gson = new Gson();
        String gameJson = gson.toJson(chessGame);
        String whiteUsername = null;
        String blackUsername = null;
        String statement =


    }

    public GameData getGame(int gameID) throws DataAccessException, SQLException, ResponseException {
        try(var conn = DatabaseManager.getConnection()){
            var Statement = "SELECT gameID, whiteUsername, blackUsername, " +
                                        "gameName, chessGame FROM gameData WHERE gameID = ?";
            try(var game = conn.prepareStatement(Statement)){
                game.setInt(1, gameID);
                try(var returnedGame = game.executeQuery()){
                    if(returnedGame.next()){
                        String whiteUsername = returnedGame.getString("whiteUsername");
                        String blackUsername = returnedGame.getString("blackUsername");
                        String gameName = returnedGame.getString("gameName");
                        String gameJson = returnedGame.getString("chessGame");
                        Gson gson = new Gson();
                        ChessGame chessGame = gson.fromJson(gameJson, ChessGame.class);
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                    }
                }
            }
        }
        catch (Exception e) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", e.getMessage()));
        }
        return null;
    }

    public void updateGame(int newGameID, GameData gameData) throws DataAccessException {

    }

    public void clearGame() throws DataAccessException {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS gameData(
            'gameID' int NOT NULL AUTO_INCREMENT,
            'whiteUsername' VARCHAR(256) DEFAULT NULL,
            'blackUsername' VARCHAR(256) DEFAULT NULL,
            'gameName' VARCHAR(256) NOT NULL,
            'chessGame' JSON DEFAULT NULL,
            PRIMARY KEY(gameID)
            )ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
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

    private Integer getGameID() throws DataAccessException, ResponseException {
        var Statement = "SELECT MIN(gameID) FROM gameData";
        try(var conn = DatabaseManager.getConnection()){
            var game = conn.prepareStatement(Statement);
            var returnedGame = game.executeQuery();
            if(returnedGame.next()){
                return returnedGame.getInt(1);
            }
        }
        catch(SQLException e){
            throw new ResponseException(500, String.format(e.getMessage()));
        }
        return 1;
    }

}
