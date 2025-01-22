package chess.MoveCalculators;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;

import chess.*;

public class PawnMovesCalc implements PieceMovesCalculator {

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard chessBoard, ChessPosition position) {
        Collection<ChessMove> moves = new ArrayList<>();
        int row = position.getRow();
        int column = position.getColumn();

        ChessPiece my_piece = chessBoard.getPiece(position);
        ChessGame.TeamColor color = my_piece.getTeamColor();

        int direction;
        if (color == ChessGame.TeamColor.BLACK) {
            direction = -1;
        } else {
            direction = 1;
        }

        ChessPosition new_position = new ChessPosition(row + direction, column);
        ChessPiece new_piece = chessBoard.getPiece(new_position);
        if (PieceMovesCalculator.isValidMove(chessBoard, position, new_position)) {
            if (new_piece == null) {
                if (row + direction == 7 || row + direction == 0) {
                    for (ChessPiece.PieceType pieceType : EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK)) {
                        ChessMove move = new ChessMove(position, new_position, pieceType);
                        moves.add(move);
                    }
                } else {
                    ChessMove move = new ChessMove(position, new_position, null);
                    moves.add(move);
                }

            }
        }

        ChessPosition new_position_1 = new ChessPosition(row + direction, column + -1);
        ChessPosition new_position_2 = new ChessPosition(row + direction, column + 1);

        ChessPiece new_piece_1 = chessBoard.getPiece(new_position_1);
        ChessPiece new_piece_2 = chessBoard.getPiece(new_position_2);

        if (PieceMovesCalculator.isValidMove(chessBoard, position, new_position_1) && new_piece_1 != null) {

            if (new_piece_1.getTeamColor() != my_piece.getTeamColor()) {
                if (row + direction == 7 || row + direction == 0) {
                    for (ChessPiece.PieceType pieceType : EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT,
                            ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK)) {
                        ChessMove move = new ChessMove(position, new_position_1, pieceType);
                        moves.add(move);
                    }
                } else {
                    ChessMove move = new ChessMove(position, new_position_1, null);
                    moves.add(move);
                }

            }
        }

        if (PieceMovesCalculator.isValidMove(chessBoard, position, new_position_2) && new_piece_2 != null) {

                if (new_piece_2.getTeamColor() != my_piece.getTeamColor()) {
                    if (row + direction == 7 || row + direction == 0) {
                        for (ChessPiece.PieceType pieceType : EnumSet.of(ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KNIGHT,
                                ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.ROOK)) {
                            ChessMove move = new ChessMove(position, new_position_2, pieceType);
                            moves.add(move);
                        }

                    }else{
                        ChessMove move = new ChessMove(position, new_position_2, null);
                    }
                }


            }

        return moves;
    }
}
