package dataaccess;

import exception.ResponseException;
import model.AuthData;

import javax.xml.crypto.Data;
import java.sql.SQLException;

public interface AuthDAO {

    void createAuth(AuthData authData) throws DataAccessException, ResponseException, SQLException;
    AuthData getAuth(String authToken) throws DataAccessException, SQLException, ResponseException;
    void deleteAuth(String authToken) throws DataAccessException, ResponseException, SQLException;
    void clearAuth() throws DataAccessException, ResponseException, SQLException;

}
