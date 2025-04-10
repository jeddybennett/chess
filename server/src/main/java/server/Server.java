package server;

import dataaccess.*;
import exception.ResponseException;
import service.ClearService;
import service.GameService;
import service.UserService;
import spark.*;
import websocket.ConnectionManager;
import websocket.WebSocketRequestHandler;


public class Server {

    public int run(int desiredPort){
        Spark.port(desiredPort);
        GameDAO gameDAO;
        UserDAO userDAO;
        AuthDAO authDAO;

        Spark.staticFiles.location("public");
        try {
            gameDAO = new MySQLGameDAO();
            userDAO = new MySQLUserDAO();
            authDAO = new MySQLAuthDAO();
        }
        catch(DataAccessException | ResponseException e){
            throw new RuntimeException(e);
        }

        ClearService clearService = new ClearService(gameDAO, authDAO, userDAO);
        GameService gameService = new GameService(gameDAO, authDAO);
        UserService userService = new UserService(userDAO, authDAO);
        ConnectionManager connectionManager = new ConnectionManager();

        UserHandler userHandler = new UserHandler(userService);
        GameHandler gameHandler = new GameHandler(gameService);
        ClearHandler clearHandler = new ClearHandler(clearService);
        ExceptionHandler exceptionHandler = new ExceptionHandler();
        WebSocketRequestHandler webSocketRequestHandler = new WebSocketRequestHandler(connectionManager, userService,
                gameService);

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", webSocketRequestHandler);
        Spark.post("/user", userHandler::registerHandler);
        Spark.post("/session", userHandler::loginHandler);
        Spark.delete("/session", userHandler::logoutHandler);

        Spark.post("/game", gameHandler::creatGameHandler);
        Spark.get("/game", gameHandler::listGameHandler);
        Spark.put("/game", gameHandler::joinGameHandler);

        Spark.delete("/db", clearHandler::clearHandler);

        Spark.exception(ResponseException.class, exceptionHandler::exceptionHandler);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
