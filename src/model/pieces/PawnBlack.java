package model.pieces;

import model.ChessPieceColor;
import model.Posn;

import java.util.Set;

public class PawnBlack extends APawn {

    public PawnBlack() {
        super(ChessPieceColor.BLACK, -1, 0);
    }

    public static Set<Posn> getPreMovesSet(int rank, int file) {
        return APawn.getPreMovesSet(rank, file, -1);
    }

    @Override
    public Set<Posn> getPreMoves(int rank, int file) {
        return APawn.getPreMovesSet(rank, file, forwardChange);
    }

    @Override
    public String toString() {
        return "^";
    }
}
