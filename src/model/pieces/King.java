package model.pieces;

import model.ChessPieceColor;
import model.ChessPieceType;
import model.Posn;

import java.util.HashSet;
import java.util.Set;

import static model.ClassicChessUtils.SIZE;

public class King extends AChessPiece {

    public King(ChessPieceColor color) {
        super(color, ChessPieceType.KING);
    }

    public static Set<Posn> getPreMovesSet(int rank, int file) {
        Set<Posn> set = new HashSet<>();
        for (int r = Math.max(rank - 1, 0); r <= Math.min(rank + 1, SIZE - 1); r ++) {
            for(int c = Math.max(file - 1, 0); c <= Math.min(file + 1, SIZE - 1); c ++) {
                if (r != 0 && c != 0) {
                    set.add(new Posn(r, c));
                }
            }
        }
        return set;
    }

    @Override
    public Set<Posn> getPreMoves(int rank, int file) {
        return King.getPreMovesSet(rank, file);
    }
}
