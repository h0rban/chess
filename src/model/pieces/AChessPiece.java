package model.pieces;

import model.Posn;
import model.Board;
import model.ChessPieceType;
import model.ChessPieceColor;

import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

import static model.ClassicChessUtils.inRange;
import static model.ClassicChessUtils.notInRange;

public abstract class AChessPiece {

    private final ChessPieceType type;
    private final ChessPieceColor color;

    protected AChessPiece(ChessPieceColor color, ChessPieceType type) {
        if (color == null || type == null) {
            throw new IllegalArgumentException("expecting non null parameters");
        }
        this.color = color;
        this.type = type;
    }

    public ChessPieceType getType() {
        return type;
    }

    public ChessPieceColor getColor() {
        return color;
    }

    public boolean readyForPromotion(int rank) {
        return false;
    }

    protected boolean isOppositeColorPiece(Board board, int rank, int file) {
        if (notInRange(rank, file)) {
            return false;
        }
        AChessPiece p = board.getBoardPieceAt(rank, file);
        return p != null && p.getColor() == getColor().next();
    }

    public Set<Posn> getLegalMoves(int rank, int file, Board board) {
        return getPreMoves(rank, file)
                .stream()
                .filter(posn -> {
                    AChessPiece p = board.getBoardPieceAt(posn);
                    return p == null || p.getColor() == color.next();
                })
                .collect(Collectors.toCollection(HashSet::new));
    }

    public Set<Posn> getLegalMoves(Posn posn, Board board) {
        return getLegalMoves(posn.rank, posn.file,board);
    }

    protected Set<Posn> getRowColChangeLegalMoves(Board board, int rank, int file, Posn ... drDcs) {
        Set<Posn> set = new HashSet<>();
        for(Posn p : drDcs) {
            int dr = p.rank;
            int df = p.file;

            rank += dr;
            file += df;
            while (inRange(rank, file) && board.cellEmpty(rank, file)) {
                set.add(new Posn(rank, file));
                rank += dr;
                file += df;
            }
            if (isOppositeColorPiece(board, rank, file)) {
                set.add(new Posn(rank, file));
            }
        }
        return set;
    }

    public abstract Set<Posn> getPreMoves(int rank, int file);

    @Override
    public String toString() {
        return this.type.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }
        if (!(o instanceof AChessPiece that)){
            return false;
        }
        return type == that.type && color == that.color;
    }

    @Override
    public int hashCode() {
        return this.type.getTypeIndex() + this.color.getColorIndex() * ChessPieceType.values().length;
    }
}
