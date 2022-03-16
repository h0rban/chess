package model.pieces;

import model.*;

import java.util.HashSet;
import java.util.Set;

import static model.ClassicChessUtils.*;

public abstract class APawn extends AChessPiece {

    private final int promotionRow;
    protected final int forwardChange;

    public APawn(ChessPieceColor color, int forwardChange, int promotionRow) {
        super(color, ChessPieceType.PAWN);
        this.forwardChange = forwardChange;
        this.promotionRow = promotionRow;
    }

    private void addIfCanEat(Board board, Set<Posn> set, int rank, int file, int df) {
        file += df;
        rank += forwardChange;
        if (isOppositeColorPiece(board, rank, file) || board.canEnPassant(rank - forwardChange, file)) {
            set.add(new Posn(rank, file));
        }
    }

    @Override
    public Set<Posn> getLegalMoves(int rank, int file, Board board) {
        Set<Posn> set = new HashSet<>();

        if (rank == SIZE - 1 || rank == 0) {
            return set;
        }

        if (inRange(rank + forwardChange, file) && board.cellEmpty(rank + forwardChange, file)) {
            set.add(new Posn(rank + forwardChange, file));
            if ((rank == 1 || rank == SIZE - 2)
                    && inRange(rank + forwardChange, file) && board.cellEmpty(rank + forwardChange * 2, file)) {
                set.add(new Posn(rank + forwardChange * 2, file));
            }
        }

        addIfCanEat(board, set, rank, file, -1);
        addIfCanEat(board, set, rank, file, 1);

        return set;
    }

    public static APawn getPawnFromColor(ChessPieceColor color) {
        if (color == null) {
            throw new IllegalArgumentException("expecting a valid color");
        }
        return switch (color) {
            case WHITE -> new PawnWhite();
            case BLACK -> new PawnBlack();
        };
    }

    protected static Set<Posn> getPreMovesSet(int rank, int file, int forwardChange) {
        Set<Posn> set = new HashSet<>();

        if (rank == SIZE - 1 || rank == 0) {
            return set;
        }
        if (rank == 1 || rank == SIZE - 2) {
            addToSetIfInRange(set, rank + forwardChange * 2, file);
        }
        for (int i = file - 1; i <= file + 1; i++) {
            addToSetIfInRange(set, rank + forwardChange, i);
        }
        return set;
    }

    @Override
    public boolean readyForPromotion(int rank) {
        return rank == promotionRow;
    }

    @Override
    public abstract String toString();
}
