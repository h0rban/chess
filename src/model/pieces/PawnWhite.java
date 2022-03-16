package model.pieces;

import model.ChessPieceColor;
import model.ClassicChessUtils;
import model.Posn;

import java.util.Set;

public class PawnWhite extends APawn {

    public PawnWhite() {
        super(ChessPieceColor.WHITE, 1, ClassicChessUtils.SIZE - 1);
    }

    public static Set<Posn> getPreMovesSet(int rank, int file) {
//        return APawn.getPreMovesSet(rank, file, forwardChange); //todo see if i can redesign this
        return APawn.getPreMovesSet(rank, file, 1);
    }

    @Override
    public Set<Posn> getPreMoves(int rank, int file) {
        return APawn.getPreMovesSet(rank, file, forwardChange);
    }

    @Override
    public String toString() {
        return "v";
    }
}
