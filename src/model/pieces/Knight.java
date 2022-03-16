package model.pieces;

import model.ChessPieceColor;
import model.ChessPieceType;
import model.Posn;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static model.ClassicChessUtils.*;

public class Knight extends AChessPiece {

    public Knight(ChessPieceColor color) {
        super(color, ChessPieceType.KNIGHT);
    }

    public static Set<Posn> getPreMovesSet(int rank, int file) {
        List<Integer> ds = Arrays.asList(-2, -1, 1, 2);
        Set<Posn> set = new HashSet<>();
        for (int d1 : ds) {
            for (int d2 : ds) {
                if (Math.abs(d1) + Math.abs(d2) == 3) {
                    addToSetIfInRange(set, rank + d1, file + d2);
                }
            }
        }
        return set;
    }


    @Override
    public Set<Posn> getPreMoves(int rank, int file) {
        return getPreMovesSet(rank, file);
    }
}
