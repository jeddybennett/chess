package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;
import org.eclipse.jetty.server.Authentication;

public class UserService {
    final private UserDAO userData;
    final private AuthDAO authData;

    public RegisterResult register(RegisterRequest registerRequest){



    }

    public LoginResult login(LoginRequest loginRequest) {
        String userName = loginRequest.userName();
        String password = loginRequest.passWord();
        try {
            if (userData.getUser(userName) == null) {
                throw
            }
        } catch (DataAccessException exception){
            throw
        }
    }

    public void logout(LogoutRequest logoutRequest) {

    }
}
