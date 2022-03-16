package model.pieces;

import model.Board;
import model.ChessPieceColor;
import model.ChessPieceType;
import model.Posn;

import java.util.HashSet;
import java.util.Set;

import static model.ClassicChessUtils.*;

public class Bishop extends AChessPiece {

    public Bishop(ChessPieceColor color) {
        super(color, ChessPieceType.BISHOP);
    }

    public static Set<Posn> getPreMovesSet(int rank, int file) {
        Set<Posn> set = new HashSet<>();
        for (int i = -Math.min(rank, file); i < SIZE - Math.max(rank, file); i++) {
            if (i != 0) {
                set.add(new Posn(rank + i, file + i));
            }
        }

        for (int i = -SIZE + 1; i < SIZE; i++) {
            if (i != 0) {
                // todo figure a more efficient way like above
                addToSetIfInRange(set, rank + i, file - i);
            }
        }
        return set;
    }

    @Override
    public Set<Posn> getLegalMoves(int rank, int file, Board board) {
        return getRowColChangeLegalMoves(board, rank, file,
                new Posn(1, 1),
                new Posn(-1, -1),
                new Posn(-1, 1),
                new Posn(1, -1)
        );
    }

    @Override
    public Set<Posn> getPreMoves(int rank, int file) {
        return Bishop.getPreMovesSet(rank, file);
    }
}
