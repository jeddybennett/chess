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
        congfigureDatabase();
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

    private void congfigureDatabase() throws ResponseException, DataAccessException {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        }
        catch (SQLException | DataAccessException exception) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", exception.getMessage()));
        }
    }

    private void executeUpdate(String statement, Object... params) throws ResponseException, DataAccessException, SQLException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(statement)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    switch (param) {
                        case String p -> preparedStatement.setString(i + 1, p);
                        case Integer p -> preparedStatement.setInt(i + 1, p);
                        case null -> preparedStatement.setNull(i + 1, NULL);
                        default -> {
                        }
                    }
                }
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
            }
        }
    }
}
