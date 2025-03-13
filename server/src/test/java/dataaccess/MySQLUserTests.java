package dataaccess;

import exception.ResponseException;
import model.UserData;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class MySQLUserTests {

    static UserDAO userDAO;

    @BeforeAll
    static void initialize() throws ResponseException{
        try{
            userDAO = new MySQLUserDAO();
        }
        catch(DataAccessException e){
            throw new ResponseException(500, e.getMessage());
        }

    }

    void clearDatabase() throws DataAccessException, ResponseException, SQLException {
        userDAO.clearUser();
    }

    @Test
    @DisplayName("Create User - Positive")
    void testCreateUserPositive() throws ResponseException, SQLException, DataAccessException{
        UserData newUser = new UserData("jedi", "password", "jedi@jedi.com");
        userDAO.createUser(newUser);
        UserData checkUser = userDAO.getUser("jedi");
        assertNotNull(checkUser);
    }

    @Test
    @DisplayName("Create User - Negative")
    void testCreateUserNegative() throws ResponseException, SQLException, DataAccessException{
        UserData firstUser = new UserData("first", "password", "first@first.com");
        UserData secondUser = new UserData("first", "password", "first@first.com");
        userDAO.createUser(firstUser);
        ResponseException exception = assertThrows(ResponseException.class, () -> {
                    userDAO.createUser(secondUser);
                });

        assertEquals(500, exception.statusCode());
    }

    @Test
    @DisplayName("Get User - Positive")
    void testGetUserPositive() throws ResponseException, SQLException, DataAccessException {
        UserData userData = new UserData("jeddy", "password", "eu@email.com");
        userDAO.createUser(userData);
        UserData getUser = userDAO.getUser("jeddy");
        assertNotNull(getUser);
        assertEquals("jeddy", getUser.username());
        assertEquals("eu@email.com", getUser.email());
        assertNotEquals("password", getUser.password());
    }

    @Test
    @DisplayName("Get User - Negative")
    void testGetUserNegative() throws ResponseException, SQLException, DataAccessException{
        UserData fakeUser = userDAO.getUser("notAUser");
        assertNull(fakeUser);
    }

    



}
