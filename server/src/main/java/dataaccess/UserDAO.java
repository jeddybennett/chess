package dataaccess;

import exception.ResponseException;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO {

    UserData getUser(String userName) throws DataAccessException, ResponseException;
    void createUser(UserData u) throws DataAccessException, ResponseException, SQLException;
    void clearUser() throws DataAccessException, ResponseException, SQLException;



}
