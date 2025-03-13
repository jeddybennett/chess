package dataaccess;

import com.google.gson.Gson;
import exception.ResponseException;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;

import static java.sql.Types.NULL;
import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class MySQLUserDAO implements UserDAO {

    public MySQLUserDAO() throws DataAccessException, ResponseException {
        configureDatabase();
    }


    public UserData getUser(String userName) throws DataAccessException, ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM userData WHERE username = ?";
            try (var user = conn.prepareStatement(statement)) {
                user.setString(1, userName);
                try (var returnedUser = user.executeQuery()) {
                    if (returnedUser.next()) {
                        String email = returnedUser.getString("email");
                        String password = returnedUser.getString("password");
                        return new UserData(userName, password, email);
                    }
                }
            }

        }
        catch (SQLException e) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", e.getMessage()));
        }
        return null;
    }

    public void createUser(UserData u) throws SQLException, DataAccessException, ResponseException {
        String username = u.username();
        String password = u.password();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String email = u.email();
        var statement = "INSERT INTO userData (username, password, email) VALUES (?, ?, ?)";
        executeUpdate(statement, username, hashedPassword, email);
    }

    public void clearUser() throws DataAccessException, ResponseException, SQLException {
        var statement = "TRUNCATE userData";
        executeUpdate(statement);
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS userData(
                username VARCHAR(256) NOT NULL,
                email VARCHAR(256) NOT NULL,
                password VARCHAR(256) NOT NULL,
                PRIMARY KEY (username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
    };

    private void configureDatabase() throws ResponseException, DataAccessException {
        MySQLGameDAO.configureDatabase(createStatements);
    }

    private void executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException, SQLException {
        MySQLGameDAO.executeUpdate(statement, params);
    }
}
