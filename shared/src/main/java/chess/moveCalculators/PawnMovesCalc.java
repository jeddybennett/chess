package chess.moveCalculators;

import chess.*;
import java.util.Collection;
import java.util.ArrayList;
import java.util.EnumSet;

public class PawnMovesCalc implements PieceMovesCalculator {
    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();

        ChessPiece myPiece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();

        int direction;
        if (myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
        } else {
            direction = -1;
        }

        //Moves forward one and promotions
        ChessPosition forwardOne = new ChessPosition(row + direction, col);
        if (PieceMovesCalculator.isValidMove(board, position, forwardOne)) {
            ChessPiece otherPiece = board.getPiece(forwardOne);
            if (otherPiece == null) {
                if (row + direction == 8 || row + direction == 1) {
                    for (ChessPiece.PieceType pieceType : EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT)) {
                        ChessMove move = new ChessMove(position, forwardOne, pieceType);
                        moves.add(move);
                    }
                } else {
                    ChessMove move = new ChessMove(position, forwardOne, null);
                    moves.add(move);
                }
            }
        }

        //Moves Forward two spaces
        if (row == 2 && myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition forwardTwoWhite = new ChessPosition(row + 2, col);
            ChessPosition forwardOneWhite = new ChessPosition(row + 1, col);

            ChessPiece twoWhite = board.getPiece(forwardTwoWhite);
            ChessPiece oneWhite = board.getPiece(forwardOneWhite);

            if (twoWhite == null && oneWhite == null) {
                ChessMove move = new ChessMove(position, forwardTwoWhite, null);
                moves.add(move);
            }
        }

        if (row == 7 && myPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition forwardTwoBlack = new ChessPosition(row - 2, col);
            ChessPosition forwardOneBlack = new ChessPosition(row - 1, col);

            ChessPiece two_black = board.getPiece(forwardTwoBlack);
            ChessPiece one_black = board.getPiece(forwardOneBlack);

            if (two_black == null && one_black == null) {
                ChessMove move = new ChessMove(position, forwardTwoBlack, null);
                moves.add(move);
            }
        }

        //Diagonal Captures and Promotions
        ChessPosition diagonalCapture1 = new ChessPosition(row + direction, col + 1);
        ChessPosition diagonalCapture2 = new ChessPosition(row + direction, col - 1);
        if (PieceMovesCalculator.isValidMove(board, position, diagonalCapture1)) {
            ChessPiece captured1 = board.getPiece(diagonalCapture1);
            if (captured1 != null) {
                if (row + direction == 8 || row + direction == 1) {
                    for (ChessPiece.PieceType pieceType : EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT)) {
                        ChessMove move = new ChessMove(position, diagonalCapture1, pieceType);
                        moves.add(move);
                    }
                } else {
                    ChessMove move = new ChessMove(position, diagonalCapture1, null);
                    moves.add(move);
                }
            }
        }
        if (PieceMovesCalculator.isValidMove(board, position, diagonalCapture2)) {
            ChessPiece captured2 = board.getPiece(diagonalCapture2);
            if (captured2 != null) {
                if (row + direction == 8 || row + direction == 1) {
                    for (ChessPiece.PieceType pieceType : EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT)) {
                        ChessMove move = new ChessMove(position, diagonalCapture2, pieceType);
                        moves.add(move);
                    }
                } else {
                    ChessMove move = new ChessMove(position, diagonalCapture2, null);
                    moves.add(move);
                }
            }
        }

        return moves;
    }
}