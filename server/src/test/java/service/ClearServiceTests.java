package service;

import dataaccess.*;
import exception.ResponseException;
import model.CreateGameRequest;
import model.CreateGameResult;
import model.RegisterRequest;
import model.RegisterResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class ClearServiceTests {

    GameDAO gameDAO;
    UserDAO userDAO;
    AuthDAO authDAO;
    ClearService clearService;
    GameService gameService;
    UserService userService;
    String authToken;

    @BeforeEach
    public void initialize() throws ResponseException, SQLException, DataAccessException {
        try {
            gameDAO = new MySQLGameDAO();
            userDAO = new MySQLUserDAO();
            authDAO = new MySQLAuthDAO();
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }

        gameDAO.clearGame();
        userDAO.clearUser();
        authDAO.clearAuth();

        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(userDAO, authDAO);
        clearService = new ClearService(gameDAO, authDAO, userDAO);
    }

    @Test
    @DisplayName("Clear - Positive")
    void positiveClear() throws ResponseException, DataAccessException, SQLException {
        RegisterRequest registerRequest = new RegisterRequest("jeddyBennett",
                "password123", "email@gmail.com");
        RegisterResult registerResult = userService.register(registerRequest);

        String authToken = registerResult.authToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("MyChessGame", authToken);
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        clearService.clear();

        assertTrue(gameDAO.listGames().isEmpty());
        assertNull(userDAO.getUser("jeddyBennett"));
        assertNull(authDAO.getAuth(authToken));

    }

}
