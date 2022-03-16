package model;

import model.pieces.*;

import java.util.Arrays;

public enum ChessPieceType {

    PAWN(null, 1), KNIGHT('N', 3), BISHOP('B', 3), ROOK('R', 5), QUEEN('Q', 8), KING('K', 0);

    private final int value;
    private final Character letter;


    ChessPieceType(Character letter, int value) {
        this.value = value;
        this.letter = letter;
    }

    public int getValue() {
        return value;
    }

    public int getTypeIndex() {
        return Arrays.asList(ChessPieceType.values()).indexOf(this);
    }

    public AChessPiece pieceFromType(ChessPieceColor color) {
        return switch (this) {
            case KING -> new King(color);
            case QUEEN -> new Queen(color);
            case KNIGHT -> new Knight(color);
            case BISHOP -> new Bishop(color);
            case ROOK -> new Rook(color);
            case PAWN -> switch (color) {
                case WHITE -> new PawnWhite();
                case BLACK -> new PawnBlack();
            };
        };
    }

    @Override
    public String toString() {
        if (this.letter == null) {
            return "";
        }
        return String.valueOf(this.letter);
    }
}
