package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.sql.Types.NULL;

public class MySQLGameDAO implements GameDAO{

    public MySQLGameDAO() throws ResponseException, DataAccessException{
        configureDatabase();
    }

    public Collection<GameData> listGames() throws DataAccessException, SQLException, ResponseException {
        String statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame from gameData where gameID = ?";
        Collection<GameData> gameList = new ArrayList<>();
        try(var conn = DatabaseManager.getConnection();
        var game = conn.prepareStatement(statement);
        var returnedGame = game.executeQuery()){
            while(returnedGame.next()){
                int gameID = returnedGame.getInt("gameID");
                String whiteUsername = returnedGame.getString("whiteUsername");
                String blackUsername = returnedGame.getString("blackUsername");
                String gameName = returnedGame.getString("gameName");
                String gameJson = returnedGame.getString("gameJson");
                ChessGame chessGame = new Gson().fromJson(gameJson, ChessGame.class);
                GameData gameData = new GameData(gameID, whiteUsername, blackUsername, gameName, chessGame);
                gameList.add(gameData);

            }
        } catch (DataAccessException | SQLException e) {
            throw new ResponseException(500, e.getMessage());
        }
        return gameList;
    }

    public int createGame(String gameName) throws DataAccessException, ResponseException, SQLException {
        int gameID = getGameID();
        ChessGame chessGame = new ChessGame();
        Gson gson = new Gson();
        String gameJson = gson.toJson(chessGame);
        String statement = "INSERT into gameData (gameID, whiteUsername, " +
                "blackUsername, gameName, gameJson) VALUES (?, ?, ?, ?, ?)";
        executeUpdate(statement, gameID, null, null, gameName, gameJson);
        return gameID;
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

    public void updateGame(int oldGameID, GameData gameData) throws DataAccessException, SQLException, ResponseException {
        String whiteUsername = gameData.whiteUsername();
        String blackUsername = gameData.blackUsername();
        String gameName = gameData.gameName();
        ChessGame chessGame = gameData.game();
        Gson gson = new Gson();
        String gameJson = gson.toJson(chessGame);
        String statement = "UPDATE gameData SET whiteUsername = ?, blackUsername = ?, gameName = ?, chessGame = ? WHERE gameID = ?";
        executeUpdate(statement, oldGameID, whiteUsername, blackUsername, gameName, gameJson);
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

    private void configureDatabase() throws ResponseException, DataAccessException {
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
        var Statement = "SELECT MAX(gameID) FROM gameData";
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

    static void executeUpdate(String statement, Object... params) throws SQLException, ResponseException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> preparedStatement.setString(i + 1, p);
                        case Integer p -> preparedStatement.setInt(i + 1, p);
                        case null -> preparedStatement.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
            }
        }
    }

}
