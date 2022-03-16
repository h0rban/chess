package model.pieces;

import model.Board;
import model.ChessPieceColor;
import model.ChessPieceType;
import model.Posn;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class Queen extends AChessPiece {

    public Queen(ChessPieceColor color) {
        super(color, ChessPieceType.QUEEN);
    }

    public static Set<Posn> getPreMovesSet(int rank, int file) {
        return Stream.of(Rook.getPreMovesSet(rank, file), Bishop.getPreMovesSet(rank, file))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public Set<Posn> getLegalMoves(int rank, int file, Board board) {
        return Stream.of(new Rook(getColor()).getLegalMoves(rank, file, board),
                        new Bishop(getColor()).getLegalMoves(rank, file, board))
                .flatMap(Collection::stream)
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public Set<Posn> getPreMoves(int rank, int file) {
        return getPreMovesSet(rank, file);
    }
}