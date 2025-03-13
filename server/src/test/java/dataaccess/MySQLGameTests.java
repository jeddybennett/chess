package dataaccess;

import chess.ChessGame;
import exception.ResponseException;
import model.GameData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLGameTests {

    static GameDAO gameDAO;

    @BeforeAll
    static void initialize() throws ResponseException, DataAccessException, SQLException{
        try{
            gameDAO = new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @BeforeEach
    void clearGameData() throws ResponseException, DataAccessException, SQLException{
        gameDAO.clearGame();
    }

    @Test
    @DisplayName("List Games - Multiple Games")
    void testListGamesMultiple() throws ResponseException, DataAccessException, SQLException{
        int firstGameID = gameDAO.createGame("First Game");
        int secondGameID = gameDAO.createGame("Second Game");
        Collection<GameData> gameList = gameDAO.listGames();
        assertNotNull(gameList);
        assertEquals(2, gameList.size());
    }

    @Test
    @DisplayName("List Games - No Games")
    void testListGamesNone() throws ResponseException, DataAccessException, SQLException{
        Collection<GameData> gameList = gameDAO.listGames();
        assertEquals(0, gameList.size());
    }

    @Test
    @DisplayName("Create Game - Positive")
    void testCreateGamePositive() throws ResponseException, DataAccessException, SQLException{
        int gameID = gameDAO.createGame("testGame");
        GameData newGame = gameDAO.getGame(gameID);
        assertNotNull(newGame);
        assertEquals(gameID, newGame.gameID());
        assertEquals("testGame", newGame.gameName());
        assertNotNull(newGame.game());
    }

    @Test
    @DisplayName("Create Game - Negative")
    void testCreateGameNegative() throws ResponseException, DataAccessException, SQLException{
        String chessName = "x".repeat(300);
        ResponseException thrownException = assertThrows(ResponseException.class, () -> {
            gameDAO.createGame(chessName);
        });

        assertEquals(500, thrownException.statusCode());
    }

    @Test
    @DisplayName("Get Game - Positive")
    void testGetGamePositive() throws ResponseException, DataAccessException, SQLException{
        int gameID = gameDAO.createGame("testGame");
        GameData gameData = gameDAO.getGame(gameID);
        assertNotNull(gameData);
        assertEquals(gameID, gameData.gameID());
        assertEquals("testGame", gameData.gameName());
        assertNotNull(gameData.game());
    }

    @Test
    @DisplayName("Get Game - Negative")
    void testGetGameNegative() throws ResponseException, DataAccessException, SQLException{
        GameData fakeGame = gameDAO.getGame(25);
        assertNull(fakeGame);
    }

    @Test
    @DisplayName("Update Game - Positive")
    void testUpdateGamePositive() throws ResponseException, DataAccessException, SQLException{
        int gameID = gameDAO.createGame("goodGame");
        GameData gameData = gameDAO.getGame(gameID);
        String blackUserName = "blackName";
        String whiteUserName = "whiteName";
        GameData updatedGame = new GameData(gameData.gameID(), whiteUserName,
                blackUserName, gameData.gameName(), gameData.game());
        gameDAO.updateGame(gameData.gameID(), updatedGame);
        GameData testData = gameDAO.getGame(gameID);
        assertNotNull(testData.blackUsername());
        assertNotNull(testData.whiteUsername());
        assertNotEquals(gameData.blackUsername(), testData.blackUsername());
        assertNotEquals(gameData.whiteUsername(), testData.whiteUsername());
        assertEquals(blackUserName, testData.blackUsername());
        assertEquals(whiteUserName, testData.whiteUsername());
    }

    @Test
    @DisplayName("Update Game - Negative")
    void testUpdateGameNegative() throws ResponseException, DataAccessException, SQLException{
        GameData fakeGame = new GameData(100, "stuff1",
                "stuff2", "fakeGame",
                new ChessGame());
        gameDAO.updateGame(100, fakeGame);
        GameData gameData = gameDAO.getGame(100);
        assertNull(gameData);
    }

    @Test
    @DisplayName("Clear Test")
    void testClearGame() throws ResponseException, DataAccessException, SQLException{
        gameDAO.createGame("firstGame");
        gameDAO.createGame("secondGame");
        gameDAO.createGame("thirdGame");
        Collection<GameData>gameList = gameDAO.listGames();
        assertEquals(3, gameList.size());

        gameDAO.clearGame();
        Collection<GameData>emptyList = gameDAO.listGames();
        assertEquals(0, emptyList.size());
        assertNotEquals(gameList.size(), emptyList.size());

    }


}
