package dataaccess;
import java.util.HashMap;
import model.AuthData;

public class MemoryAuthDAO implements AuthDAO{

    final private HashMap<String, AuthData> authInfo = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authInfo.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authInfo.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authInfo.remove(authToken);
    }

    @Override
    public void clearAuth(){
        authInfo.clear();
    }
}
