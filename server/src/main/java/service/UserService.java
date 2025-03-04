package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import java.util.UUID;

import exception.ResponseException;
import model.*;

public class UserService {
    private final UserDAO userDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public RegisterResult register(RegisterRequest registerRequest) throws ResponseException {

        String email = registerRequest.email();
        String password = registerRequest.password();
        String username = registerRequest.username();


        if (email == null || password == null || username == null) {
            throw new ResponseException(400, "Error: bad request");
        }

        try {
            UserData currentUser = userDAO.getUser(username);
            if (currentUser != null) {
                throw new ResponseException(403, "Error: already taken");
            }

            userDAO.createUser(new UserData(username, password, email));
            String authToken = generateToken();
            authDAO.createAuth(new AuthData(authToken,username));

            return new RegisterResult(authToken, username);

        }
        catch (DataAccessException exception) {
            throw new ResponseException(500, "Error:" + exception.getMessage());
        }

    }

    public LoginResult login(LoginRequest loginRequest) throws ResponseException {

        try {
            String username = loginRequest.username();
            String password = loginRequest.password();
            UserData currentUser = userDAO.getUser(username);

            if (currentUser == null || !currentUser.password().equals(password)) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            String authToken = generateToken();
            authDAO.createAuth(new AuthData(authToken, username));
            return new LoginResult(authToken, username);

        }
        catch (DataAccessException exception) {
            throw new ResponseException(500, "Error:" + exception.getMessage());
        }
    }

    public void logout(LogoutRequest logoutRequest) throws ResponseException {
        String authToken = logoutRequest.authToken();
        try {
            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            authDAO.deleteAuth(authToken);

        }
        catch (DataAccessException exception) {
            throw new ResponseException(500, "Error:" + exception.getMessage());
        }
    }
}
