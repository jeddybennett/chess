package model;
import chess.*;

public record JoinGameRequest (String authToken, ChessGame.TeamColor teamColor, int gameID){ ;
}
