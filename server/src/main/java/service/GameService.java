package service;

import chess.ChessGame;
import dataaccess.*;
import dataaccess.DataAccessException;

import java.util.Collection;

import exception.ResponseException;
import model.*;

public class GameService {
    final private GameDAO gameDAO;
    final private AuthDAO authDAO;

    public GameService(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ListGameResult ListGames(ListGameRequest listGameRequest) throws ResponseException{
        String authToken = listGameRequest.authToken();

        try {
            if (authToken == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

            return new ListGameResult(gameDAO.listGames());
        }
        catch(DataAccessException exception){
            throw new ResponseException(500, "Error: " + exception.getMessage());


        }

    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) throws ResponseException, DataAccessException {
        String authToken = createGameRequest.authToken();
        String gameName = createGameRequest.gameName();
        try {
            if (gameName == null) {
                throw new ResponseException(400, "Error: bad request");
            } else if (authToken == null) {
                throw new ResponseException(401, "Error: unauthorized");
            }

        GameData gameData = gameDAO.createGame(gameName);
        int gameID = gameData.gameID();
        return new CreateGameResult(gameID);

        } catch (DataAccessException exception){
            throw new ResponseException(500, "Error: " + exception.getMessage());
        }
    }

    public void joinGame(JoinGameRequest joinGameRequest) throws ResponseException, DataAccessException {

        String authToken = joinGameRequest.authToken();
        ChessGame.TeamColor teamColor = joinGameRequest.teamColor();
        int gameID = joinGameRequest.gameID();;
        GameData gameData = gameDAO.getGame(gameID);
        String blackUsername = gameData.blackUsername();
        String whiteUsername = gameData.whiteUsername();

        try{
            if(authToken == null){
                throw new ResponseException(401, "Error: unauthorized");
            }
            else if(teamColor == null || gameData == null){
                throw new ResponseException(400, "Error: bad request");
            }
            else if(blackUsername!=null && teamColor == ChessGame.TeamColor.BLACK || whiteUsername!=null && teamColor == ChessGame.TeamColor.WHITE){
                throw new ResponseException(403, "Error: already taken");
            }
            ChessGame chessGame = gameData.game();
            String gameName = gameData.gameName();

            AuthData authData = authDAO.getAuth(authToken);
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
