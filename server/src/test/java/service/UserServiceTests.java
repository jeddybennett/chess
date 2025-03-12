package service;

import dataaccess.*;
import exception.ResponseException;
import dataaccess.DataAccessException;
import model.*;
import exception.ResponseException;

import org.junit.jupiter.api.*;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    private UserService userService;


    @BeforeEach
    void initialize() throws ResponseException {
        UserDAO userDAO;
        AuthDAO authDAO;
        try {
            userDAO = new MySQLUserDAO();
            authDAO = new MySQLAuthDAO();
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
        userService = new UserService(userDAO, authDAO);
    }

    @Test
    @DisplayName("Register - Positive")
    void testRegisterPositive() throws ResponseException, DataAccessException{
        RegisterRequest registerRequest = new RegisterRequest("jeddyBennett",
                "password123", "email@gmail.com");
        RegisterResult registerResult = userService.register(registerRequest);
        assertNotNull(registerResult);
        assertNotNull(registerResult.authToken());
        assertEquals("jeddyBennett", registerResult.username());
    }

    @Test
    @DisplayName("Register - Negative")
    void testRegisterNegative() throws ResponseException, DataAccessException{
        RegisterRequest registerRequest1 = new RegisterRequest("duplicated",
                "password123", "email@gmail.com");
        RegisterResult registerResult1 = userService.register(registerRequest1);

        RegisterRequest registerRequest2 = new RegisterRequest("duplicated",
                "password", "newemail#@gmail.com");
        ResponseException thrownException = assertThrows(ResponseException.class, () -> userService.register(registerRequest2));
        assertEquals(403, thrownException.statusCode());
        assertTrue(thrownException.getMessage().contains("already taken"));
    }

    @Test
    @DisplayName("Login - Positive")
    void testLoginPositive() throws ResponseException, DataAccessException, SQLException {
        RegisterRequest registerRequest = new RegisterRequest("jeddyBennett",
                "password123", "email@gmail.com");
        RegisterResult registerResult = userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("jeddyBennett", "password123");
        LoginResult loginResult = userService.login(loginRequest);

        assertEquals("jeddyBennett", loginResult.username());
        assertNotNull(loginResult.authToken());
    }

    @Test
    @DisplayName("Login - Negative (wrong username)")
    void testLoginNegative() throws ResponseException, DataAccessException{
        LoginRequest loginRequest = new LoginRequest("eddyBennett", "password123");
        ResponseException thrownException = assertThrows(
                ResponseException.class,
                () -> userService.login(loginRequest)
        );
        assertEquals(401, thrownException.statusCode());
        assertTrue(thrownException.getMessage().contains("unauthorized"));
    }

    @Test
    @DisplayName("Logout - Positive")
    void testLogoutPositive() throws ResponseException, DataAccessException, SQLException {
        RegisterRequest registerRequest = new RegisterRequest("newUser", "password1234", "hello@gmail.com");
        RegisterResult registerResult = userService.register(registerRequest);
        LoginRequest loginRequest = new LoginRequest("newUser", "password1234");
        LoginResult loginResult = userService.login(loginRequest);

        String authToken =loginResult.authToken();
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        assertNull(null);
    }

    @Test
    @DisplayName("Logout - Negative (Invalid Token)")
    void testLogoutNegative() throws ResponseException, DataAccessException{

        String authToken = "notARealAuthToken";
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        ResponseException thrownException = assertThrows(
                ResponseException.class,
                () -> userService.logout(logoutRequest)
        );
        assertEquals(401, thrownException.statusCode());
        assertTrue(thrownException.getMessage().contains("unauthorized"));

    }





}
