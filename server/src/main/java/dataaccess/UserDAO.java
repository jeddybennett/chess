package dataaccess;

import model.UserData;

public interface UserDAO {

    UserData getUser(String userName) throws DataAccessException;
    void createUser(UserData u) throws DataAccessException;
    void clearUser() throws DataAccessException;



}
