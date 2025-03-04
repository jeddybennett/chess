package service;

import chess.ChessGame;
import dataaccess.*;
import exception.ResponseException;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.module.ResolutionException;

import static org.junit.jupiter.api.Assertions.*;

public class GameServiceTests {

    private GameDAO gameDAO;
    private GameService gameService;
    private String authToken;

    @BeforeEach
    void initialize() throws ResponseException{
        UserDAO userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        AuthDAO authDAO = new MemoryAuthDAO();

        UserService userService = new UserService(userDAO, authDAO);
        gameService = new GameService(gameDAO, authDAO);

        RegisterRequest registerRequest = new RegisterRequest("jeddyBennett",
                "password123", "email@gmail.com");
        RegisterResult registerResult = userService.register(registerRequest);
        authToken = registerResult.authToken();
    }

    @Test
    @DisplayName("Create Game - Positive")
    void createGamePositive() throws ResponseException, DataAccessException {
        CreateGameRequest createGameRequest = new CreateGameRequest("MyChessGame", authToken);
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

        assertNotNull(createGameResult);
        GameData newGame = gameDAO.getGame(createGameResult.gameID());
        assertNotNull(newGame);
        assertEquals("MyChessGame", newGame.gameName());
    }

    @Test
    @DisplayName("Create Game - Negative (No Game Name)")
    void createGameNegative() throws ResponseException{
        CreateGameRequest createGameRequest = new CreateGameRequest(null, authToken);
        ResponseException thrownException = assertThrows(
                ResponseException.class, () -> gameService.createGame(createGameRequest)
        );
        assertEquals(400, thrownException.statusCode());
        assertTrue(thrownException.getMessage().contains("bad request"));
    }

    @Test
    @DisplayName("Join Game - Positive")
    void joinGamePositive() throws ResponseException, DataAccessException {
        CreateGameRequest createGameRequest = new CreateGameRequest("MyChessGame", authToken);
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

        int gameID = createGameResult.gameID();
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, gameID);
        gameService.joinGame(joinGameRequest);

        GameData currentGame = gameDAO.getGame(gameID);
        assertEquals("jeddyBennett", currentGame.whiteUsername());
        assertNull(currentGame.blackUsername());
    }

    @Test
    @DisplayName("Join Game - Negative (Already Taken)")
    void joinGameNegative() throws ResponseException, DataAccessException{
        CreateGameRequest createGameRequest = new CreateGameRequest("MyChessGame", authToken);
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

        int gameID = createGameResult.gameID();
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, gameID);
        gameService.joinGame(joinGameRequest);

        JoinGameRequest badRequest = new JoinGameRequest(authToken, ChessGame.TeamColor.WHITE, gameID);
        ResponseException thrownException = assertThrows(
                ResponseException.class, () -> gameService.joinGame(badRequest)
                );
        assertEquals(403, thrownException.statusCode());
        assertTrue(thrownException.getMessage().contains("already taken"));
    }


    @Test
    @DisplayName("List Games - Positive")
    void listGamesPositive() throws ResponseException, DataAccessException {
        gameService.createGame(new CreateGameRequest("ChessGame1", authToken));
        gameService.createGame(new CreateGameRequest("ChessGame2", authToken));
        gameService.createGame(new CreateGameRequest("ChessGame3", authToken));

        ListGameRequest listGameRequest = new ListGameRequest(authToken);
        ListGameResult listGameResult = gameService.listGames(listGameRequest);
        assertNotNull(listGameResult);
        assertFalse(listGameResult.games().isEmpty());
        assertEquals(3, listGameResult.games().size());
    }

    @Test
    @DisplayName("List Games - Negative (Invalid Token)")
    void listGamesNegative() throws ResponseException{
        String invalidToken = "notAValidToken";
        ListGameRequest listGameRequest = new ListGameRequest(invalidToken);
        ResponseException thrownException = assertThrows(
                ResponseException.class, () -> gameService.listGames(listGameRequest)
        );
        assertEquals(401, thrownException.statusCode());
        assertTrue(thrownException.getMessage().contains("unauthorized"));
    }
}
