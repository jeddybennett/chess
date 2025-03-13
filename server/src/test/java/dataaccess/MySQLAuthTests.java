package dataaccess;

import exception.ResponseException;
import model.AuthData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLAuthTests {

    static AuthDAO authDAO;

    @BeforeAll
    static void initialize() throws ResponseException, DataAccessException, SQLException{
        try{
            authDAO = new MySQLAuthDAO();
        } catch (DataAccessException e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    @BeforeEach
    void clearAuthDatabase() throws ResponseException, DataAccessException, SQLException{
        authDAO.clearAuth();
    }

    @Test
    @DisplayName("Create Auth - Positive")
    void testCreateAuthPositive() throws ResponseException, DataAccessException, SQLException{
        AuthData authData = new AuthData("authToken", "jeddy");
        authDAO.createAuth(authData);
        AuthData getAuth = authDAO.getAuth("authToken");
        assertNotNull(getAuth);
        assertEquals("authToken", getAuth.authToken());
        assertEquals("jeddy", getAuth.username());
    }

    @Test
    @DisplayName("Create Auth - Negative")
    void testCreateAuthNegative() throws ResponseException, DataAccessException, SQLException{
        AuthData firstAuth = new AuthData("token", "jeddy");
        AuthData secondAuth = new AuthData("token", "other");
        authDAO.createAuth(firstAuth);

        ResponseException thrownException = assertThrows(ResponseException.class, () -> {
            authDAO.createAuth(secondAuth);
        });
        assertEquals(500, thrownException.statusCode());
    }

    @Test
    @DisplayName("Get Auth - Positive")
}
