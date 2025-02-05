package chess.MoveCalculators;

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
        ChessPosition forward_one = new ChessPosition(row + direction, col);
        if (PieceMovesCalculator.isValidMove(board, position, forward_one)) {
            ChessPiece otherPiece = board.getPiece(forward_one);
            if (otherPiece == null) {
                if (row + direction == 8 || row + direction == 1) {
                    for (ChessPiece.PieceType pieceType : EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT)) {
                        ChessMove move = new ChessMove(position, forward_one, pieceType);
                        moves.add(move);
                    }
                } else {
                    ChessMove move = new ChessMove(position, forward_one, null);
                    moves.add(move);
                }
            }
        }

        //Moves Forward two spaces
        if (row == 2 && myPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            ChessPosition forward_two_white = new ChessPosition(row + 2, col);
            ChessPosition forward_one_white = new ChessPosition(row + 1, col);

            ChessPiece two_white = board.getPiece(forward_two_white);
            ChessPiece one_white = board.getPiece(forward_one_white);

            if (two_white == null && one_white == null) {
                ChessMove move = new ChessMove(position, forward_two_white, null);
                moves.add(move);
            }
        }

        if (row == 7 && myPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            ChessPosition forward_two_black = new ChessPosition(row - 2, col);
            ChessPosition forward_one_black = new ChessPosition(row - 1, col);

            ChessPiece two_black = board.getPiece(forward_two_black);
            ChessPiece one_black = board.getPiece(forward_one_black);

            if (two_black == null && one_black == null) {
                ChessMove move = new ChessMove(position, forward_two_black, null);
                moves.add(move);
            }
        }

        //Diagonal Captures and Promotions
        ChessPosition diagonal_capture_1 = new ChessPosition(row + direction, col + 1);
        ChessPosition diagonal_capture_2 = new ChessPosition(row + direction, col - 1);
        if (PieceMovesCalculator.isValidMove(board, position, diagonal_capture_1)) {
            ChessPiece captured_1 = board.getPiece(diagonal_capture_1);
            if (captured_1 != null) {
                if (row + direction == 8 || row + direction == 1) {
                    for (ChessPiece.PieceType pieceType : EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT)) {
                        ChessMove move = new ChessMove(position, diagonal_capture_1, pieceType);
                        moves.add(move);
                    }
                } else {
                    ChessMove move = new ChessMove(position, diagonal_capture_1, null);
                    moves.add(move);
                }
            }
        }
        if (PieceMovesCalculator.isValidMove(board, position, diagonal_capture_2)) {
            ChessPiece captured_2 = board.getPiece(diagonal_capture_2);
            if (captured_2 != null) {
                if (row + direction == 8 || row + direction == 1) {
                    for (ChessPiece.PieceType pieceType : EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.ROOK,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT)) {
                        ChessMove move = new ChessMove(position, diagonal_capture_2, pieceType);
                        moves.add(move);
                    }
                } else {
                    ChessMove move = new ChessMove(position, diagonal_capture_2, null);
                    moves.add(move);
                }
            }
        }

        return moves;
    }
}