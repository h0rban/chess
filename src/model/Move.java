package model;

import model.pieces.AChessPiece;

public class Move {

    public final Posn from;
    public final Posn to;
    public final AChessPiece piece;
    public final boolean pawnDouble;

    public Move(AChessPiece piece, Posn from, Posn to) {
        if (piece == null || from == null || to == null) {
            throw new IllegalArgumentException();
        }
        this.piece = piece;
        this.from = from;
        this.to = to;
        pawnDouble = piece.getType() == ChessPieceType.PAWN && Math.abs(from.rank - to.rank) == 2;
    }

    public Move(AChessPiece piece, int r0, int f0, int r1, int f1) {
        this(piece, new Posn(r0, f0), new Posn(r1, f1));
    }
}
