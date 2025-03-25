package client;

import exception.ResponseException;
import model.LoginRequest;
import model.LoginResult;
import model.RegisterRequest;
import model.RegisterResult;
import net.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;
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
    void positiveTestRegister{
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
    public void negativeTestRegister{
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
    void positiveTestLogin{
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
    void negativeTestLogin{
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
    void positiveTestLogout{
        String username = "newUser";
        String password = "goodTimes";
        facade.register(new RegisterRequest(username, password, "email@email.com"));
        LoginResult loginResult = facade.login(new LoginRequest(username, password));
        String authToken = loginResult.authToken();
        assertDoesNotThrow(facade.logout(authToken));
    }






}
