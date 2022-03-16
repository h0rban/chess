package model.pieces;

import model.Board;
import model.ChessPieceColor;
import model.ChessPieceType;
import model.Posn;

import java.util.HashSet;
import java.util.Set;

import static model.ClassicChessUtils.SIZE;

public class Rook extends AChessPiece {

    public Rook(ChessPieceColor color) {
        super(color, ChessPieceType.ROOK);
    }

    public static Set<Posn> getPreMovesSet(int rank, int file) {
        Set<Posn> set = new HashSet<>();
        for (int r = 0; r <= SIZE - 1; r++) {
            if (r != rank) {
                set.add(new Posn(r, file));
            }
        }
        for (int c = 0; c < SIZE - 1; c++) {
            if (c != file) {
                set.add(new Posn(rank, c));
            }
        }
        return set;
    }

    @Override
    public Set<Posn> getLegalMoves(int rank, int file, Board board) {
        return getRowColChangeLegalMoves(board, rank, file,
                new Posn(1, 0),
                new Posn(0, 1),
                new Posn(-1, 0),
                new Posn(0, -1)
        );
    }

    @Override
    public Set<Posn> getPreMoves(int rank, int file) {
        return Rook.getPreMovesSet(rank, file);
    }
}
