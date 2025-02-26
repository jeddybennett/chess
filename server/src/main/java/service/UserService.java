package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;

import java.util.Objects;
import java.util.UUID;

import exception.ResponseException;
import model.*;
import exception.*;
import org.eclipse.jetty.server.Authentication;

public class UserService {
    final private UserDAO userData;
    final private AuthDAO authData;

    public UserService(UserDAO userData, AuthDAO authData) {
        this.userData = userData;
        this.authData = authData;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException, ResponseException {
        String email = registerRequest.email();
        String password = registerRequest.passWord();
        String username = registerRequest.userName();

        UserData current_user = userData.getUser(username);
        try {
            if (email == null || password == null || username == null) {
                throw new ResponseException(400, "Error: bad request");
            } else if (current_user.username() != null) {
                throw new ResponseException(403, "Error: already taken");
            }

            userData.createUser(new UserData(username, password, email));
            String authToken = generateToken();
            return new RegisterResult(authToken, username);

        } catch (DataAccessException exception) {
            throw new ResponseException(500, "Error:" + exception.getMessage());
        }

    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException, ResponseException {
        String username = loginRequest.userName();
        String password = loginRequest.passWord();

        UserData current_user = userData.getUser(username);

        try {
            if (current_user.username() == null) {
                throw new ResponseException(500, "Error: username doesn't exist");
            } else if (!Objects.equals(password, current_user.password())) {
                throw new ResponseException(401, "Error: unauthorized");
            } else {
                String authToken = generateToken();
                authData.createAuth(new AuthData(authToken, username));
                return new LoginResult(authToken, username);
            }
        } catch (DataAccessException exception) {
            throw new ResponseException(500, "Error:" + exception.getMessage());
        }
    }

    public void logout(LogoutRequest logoutRequest) throws DataAccessException, ResponseException {
        String authToken = logoutRequest.authToken();
        try {
            if (authToken == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            authData.deleteAuth(authToken);

        } catch (DataAccessException exception) {
            throw new ResponseException(500, "Error:" + exception.getMessage());
        }
    }
}
