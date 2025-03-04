package dataaccess;
import java.util.HashMap;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{

    final private HashMap<String, AuthData> auth_info = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        auth_info.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return auth_info.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        auth_info.remove(authToken);
    }

    @Override
    public void clearAuth(){
        auth_info.clear();
    }
}
