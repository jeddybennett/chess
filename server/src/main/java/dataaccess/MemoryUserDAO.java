package dataaccess;

import model.UserData;
import java.util.HashMap;

public class MemoryUserDAO implements UserDAO {

    private final HashMap<String, UserData> users = new HashMap<>();

    public UserData getUser(String userName) throws DataAccessException {
        return users.get(userName);
    }

    public void createUser(UserData u) throws DataAccessException {
        users.put(u.username(), u);
    }

    public void clearUser() throws DataAccessException {
        users.clear();
    }
}
