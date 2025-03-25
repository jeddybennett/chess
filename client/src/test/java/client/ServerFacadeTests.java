package client;

import chess.ChessGame;
import exception.ResponseException;
import model.*;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        String serverURL = "http://localhost:" + port;
        facade = new ServerFacade(serverURL);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clear() throws ResponseException {
        facade.clear();
    }

    @Test
    @DisplayName("Register - Positive")
    void positiveTestRegister() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("Jeddy",
                "Bennett", "bennett@c.com");
        RegisterResult registerResult = facade.register(registerRequest);
        String authToken = registerResult.authToken();
        String username = registerResult.username();
        assertNotNull(authToken);
        assertNotNull(username);
        assertEquals("Jeddy", username);
        assertTrue(authToken.length() > 10);

    }

    @Test
    @DisplayName("Register - Negative")
    public void negativeTestRegister() throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest("user1",
                "password", "s@s.com");
        RegisterResult registerResult1 = facade.register(registerRequest);
        ResponseException ex = assertThrows(ResponseException.class, () -> {
            facade.register(registerRequest);
        });
        assertEquals(500, ex.statusCode());
    }

    @Test
    @DisplayName("Login - Positive")
    void positiveTestLogin() throws ResponseException {
        String username = "new";
        String password = "user";
        RegisterRequest registerRequest = new RegisterRequest(username,password, "stufff@help.com");
        RegisterResult registerResult = facade.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest(username, password);
        LoginResult loginResult = facade.login(loginRequest);
        String authToken = loginResult.authToken();
        String postLogin = loginResult.username();
        assertEquals(username, postLogin);
        assertNotNull(authToken);
    }

    @Test
    @DisplayName("Login - Negative")
    void negativeTestLogin() throws ResponseException {
        facade.register(new RegisterRequest("new", "user", "user@user.com"));
        LoginRequest login1 = new LoginRequest("new", "badPassword");
        LoginRequest login2 = new LoginRequest("badUsername", "doesnotMatter");
        assertThrows(ResponseException.class, ()->{
            facade.login(login1);
        });
        assertThrows(ResponseException.class, ()->{
            facade.login(login2);
        });
    }

    @Test
    @DisplayName("Logout - Positive")
    void positiveTestLogout() throws ResponseException {
        String username = "newUser";
        String password = "goodTimes";
        facade.register(new RegisterRequest(username, password, "email@email.com"));
        LoginResult loginResult = facade.login(new LoginRequest(username, password));
        String authToken = loginResult.authToken();
        assertNotNull(authToken);
        assertDoesNotThrow(() -> facade.logout(authToken));
    }

    @Test
    @DisplayName("Logout - Negative")
    void negativeTestLogout(){
        String badToken = "thisIsAFakeToken";
        ResponseException ex = assertThrows(ResponseException.class, ()->{
            facade.logout(badToken);
        });
        assertEquals(500, ex.statusCode());
    }

    @Test
    @DisplayName("Create Game - Positive")
    void positiveTestCreateGame() throws ResponseException {
        facade.register(new RegisterRequest("new", "user", "ugh@ugh.com"));
        LoginResult loginResult = facade.login(new LoginRequest("new", "user"));
        String authToken = loginResult.authToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("GAME1", authToken);
        CreateGameResult createGameResult = facade.createGame(createGameRequest);
        int gameID = createGameResult.gameID();
        assertNotNull(createGameResult);
        assertTrue(gameID > 0);
    }


    @Test
    @DisplayName("Create Game - Negative")
    void negativeTestCreateGame(){
        String badToken = "fakeToken";
        CreateGameRequest createGameRequest = new CreateGameRequest("GAME", badToken);
        ResponseException ex = assertThrows(ResponseException.class, ()->{
            facade.createGame(createGameRequest);
        });
        assertEquals(500, ex.statusCode());
    }

    @Test
    @DisplayName("Join Game - Positive")
    void positiveTestJoinGame() throws ResponseException {
        facade.register(new RegisterRequest("new", "user", "woo@woo.com"));
        LoginResult loginResult = facade.login(new LoginRequest("new", "user"));
        String authToken = loginResult.authToken();
        CreateGameResult createGameResult = facade.createGame(new CreateGameRequest(
                "Game1", authToken));
        int gameID = createGameResult.gameID();
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.BLACK;
        assertTrue(gameID > 0);
        assertDoesNotThrow(()->facade.joinGame(new JoinGameRequest(authToken, playerColor, gameID)));
    }

    @Test
    @DisplayName("Join Game - Negative")
    void negativeTestJoinGame() throws ResponseException {
        facade.register(new RegisterRequest("test", "user", "woo@woo.com"));
        LoginResult loginResult = facade.login(new LoginRequest("test", "user"));
        String authToken = loginResult.authToken();
        CreateGameRequest createGameRequest = new CreateGameRequest("Game1", authToken);
        CreateGameResult createGameResult = facade.createGame(createGameRequest);
        int gameID = createGameResult.gameID();
        ChessGame.TeamColor playerColor = ChessGame.TeamColor.BLACK;
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, 90000);
        ResponseException ex = assertThrows(ResponseException.class, ()->{
            facade.joinGame(joinGameRequest);
        });
        assertEquals(500, ex.statusCode());
    }

    @Test
    @DisplayName("List Game - Positive")
    void positiveListGame() throws ResponseException {
        String username = "try";
        String password = "password";
        facade.register(new RegisterRequest(username, password, "email@email.com"));
        LoginResult loginResult = facade.login(new LoginRequest(username, password));
        String authToken = loginResult.authToken();
        facade.createGame(new CreateGameRequest("GAME1", authToken));
        facade.createGame(new CreateGameRequest("GAME2", authToken));
        facade.createGame(new CreateGameRequest("GAME3", authToken));
        ListGameResult listGameResult = facade.listGames(new ListGameRequest(authToken));
        Collection<GameData> gameInfo = listGameResult.games();
        assertNotNull(gameInfo);
        assertEquals(3, gameInfo.size());
    }

    @Test
    @DisplayName("List Game - Negative")
    void negativeListGame(){
        String badToken = "badToken";
        ResponseException ex = assertThrows(ResponseException.class, ()->{
            facade.listGames(new ListGameRequest(badToken));
        });
        assertEquals(500, ex.statusCode());
    }

    @Test
    @DisplayName("Clear Test")
    void clearGame() throws ResponseException {
        facade.register(new RegisterRequest("user1", "password1", "email1@email.com"));
        facade.register(new RegisterRequest("user2", "password2", "email2@email.com"));
        LoginResult login1 = facade.login(new LoginRequest("user1", "password1"));
        LoginResult login2 = facade.login(new LoginRequest("user2", "password2"));
        String auth1 = login1.authToken();
        String auth2 = login2.authToken();
        CreateGameResult createGameResult = facade.createGame(new CreateGameRequest("GAME1", auth1));
        facade.createGame(new CreateGameRequest("GAME2", auth2));
        facade.clear();
        assertThrows(ResponseException.class, ()->{
            facade.login(new LoginRequest("user1", "password1"));
        });
        assertThrows(ResponseException.class, ()->{
            facade.joinGame(new JoinGameRequest(auth1, ChessGame.TeamColor.BLACK, createGameResult.gameID()));
        });
    }








}
