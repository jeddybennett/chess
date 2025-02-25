package dataaccess;
import model.*;

public interface UserDAO {

    UserData getUser(String userName) throws DataAccessException;
    void createUser(UserData u) throws DataAccessException;
    void clear() throws DataAccessException;



}
