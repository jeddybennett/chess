package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.DataAccessException;

import exception.ResponseException;
import model.*;

import java.sql.SQLException;

public class GameService {
    final private GameDAO gameDAO;
    final private AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGameResult listGames(ListGameRequest listGameRequest) throws DataAccessException, ResponseException, SQLException{
        String authToken = listGameRequest.authToken();
        AuthData authData = authDAO.getAuth(authToken);
        try {
            if (authData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            return new ListGameResult(gameDAO.listGames());
        }
        catch(DataAccessException exception){
            throw new ResponseException(500, "Error: " + exception.getMessage());


        }

    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException, SQLException{
        String authToken = createGameRequest.authToken();
        String gameName = createGameRequest.gameName();

        try {
            if (gameName == null || authToken == null) {
                throw new ResponseException(400, "Error: bad request");
            }

            AuthData authData = authDAO.getAuth(authToken);
            if (authData == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

        int gameID = gameDAO.createGame(gameName);
        return new CreateGameResult(gameID);

        } catch (DataAccessException exception){
            throw new ResponseException(500, "Error: " + exception.getMessage());
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException, SQLException {

        String authToken = joinGameRequest.authToken();
        ChessGame.TeamColor teamColor = joinGameRequest.playerColor();
        int gameID = joinGameRequest.gameID();;

        try{
            if(authToken == null || gameID <= 0 || teamColor == null){
                throw new ResponseException(400, "Error: bad request");
            }
            AuthData authData = authDAO.getAuth(authToken);

            if(authData == null){
                throw new ResponseException(401, "Error: unauthorized");
            }

            GameData gameData = gameDAO.getGame(gameID);
            String blackUsername = gameData.blackUsername();
            String whiteUsername = gameData.whiteUsername();

            if(blackUsername!=null && teamColor == ChessGame.TeamColor.BLACK || whiteUsername!=null && teamColor == ChessGame.TeamColor.WHITE){
                throw new ResponseException(403, "Error: already taken");
            }

            ChessGame chessGame = gameData.game();
            String gameName = gameData.gameName();
            String userName = authData.username();
            GameData joinedGame;

            if(teamColor == ChessGame.TeamColor.BLACK){
                joinedGame = new GameData(gameID, whiteUsername, userName, gameName, chessGame) ;
            }
            else{
                joinedGame = new GameData(gameID, userName, blackUsername, gameName, chessGame) ;
            }

            gameDAO.updateGame(gameID, joinedGame);

        }
        catch(DataAccessException exception){
            throw new ResponseException(500, "Error: " + exception.getMessage());
        }
    }
}
