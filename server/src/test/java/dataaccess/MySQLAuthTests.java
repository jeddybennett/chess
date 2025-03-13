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
    void testGetAuthPositive() throws ResponseException, DataAccessException, SQLException{
        AuthData authData = new AuthData("authToken", "jeddy");
        authDAO.createAuth(authData);
        AuthData getAuth = authDAO.getAuth("authToken");
        assertNotNull(getAuth);
        assertEquals("authToken", getAuth.authToken());
        assertEquals("jeddy", getAuth.username());
    }

    @Test
    @DisplayName("Get Auth - Negative")
    void testGetAuthNegative() throws ResponseException, DataAccessException, SQLException{
        AuthData authData = new AuthData("token", "jeddy");
        authDAO.createAuth(authData);
        AuthData fakeAuth = authDAO.getAuth("fakeToken");
        assertNull(fakeAuth);
        assertNotEquals("fakeToken", authData.authToken());
    }

    @Test
    @DisplayName("Delete Auth - Positive")
    void testDeleteAuthPositive() throws ResponseException, DataAccessException, SQLException{
        AuthData firstData = new AuthData("token1", "firstName");
        AuthData secondData = new AuthData("token2", "secondName");
        authDAO.createAuth(firstData);
        authDAO.createAuth(secondData);

        AuthData getFirst = authDAO.getAuth("token1");
        AuthData getSecond = authDAO.getAuth("token2");
        assertNotNull(getFirst);
        assertNotNull(getSecond);

        authDAO.deleteAuth("token1");
        authDAO.deleteAuth("token2");

        AuthData deletedFirst = authDAO.getAuth("token1");
        AuthData deletedSecond = authDAO.getAuth("token2");
        assertNull(deletedFirst);
        assertNull(deletedSecond);
    }

    @Test
    @DisplayName("Delete Auth - Negative")
    void testDeleteAuthNegative() throws ResponseException, DataAccessException, SQLException{
        try{
        assertDoesNotThrow(() -> authDAO.deleteAuth("tokenDoesNotExist"));
    }
        catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
}
    @Test
    @DisplayName("Clear Auth Test")
    void testClearAuth() throws ResponseException, DataAccessException, SQLException{
        AuthData firstAuth = new AuthData("token1", "name1");
        AuthData secondAuth = new AuthData("token2", "name2");
        AuthData thirdAuth = new AuthData("token3", "name3");
        authDAO.createAuth(firstAuth);
        authDAO.createAuth(secondAuth);
        authDAO.createAuth(thirdAuth);

        authDAO.clearAuth();
        AuthData deletedFirst = authDAO.getAuth("token1");
        AuthData deletedSecond = authDAO.getAuth("token2");
        AuthData deletedThird = authDAO.getAuth("token3");

        assertNull(deletedFirst);
        assertNull(deletedSecond);
        assertNull(deletedThird);
    }
}

