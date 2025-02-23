package dataaccess;
import model.*;

public interface UserDAO {

    void getUser(UserData u) throws DataAccessException;
    void createUser(UserData u) throws DataAccessException;
    void clear() throws DataAccessException;



}
