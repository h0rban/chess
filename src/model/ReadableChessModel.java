package model;

import java.util.Set;

public interface ReadableChessModel<K> extends ChessModel<K> {

    K getPieceAt(String file, int rank);

    void removePiece(String file, int rank) throws IllegalArgumentException, IllegalStateException;

    void move(String fileFrom, int rankFrom, String fileTo, int rankTo) throws IllegalArgumentException, IllegalStateException;

    Set<Posn> getPreMoves(String file, int rank) throws IllegalArgumentException, IllegalStateException;

    Set<Posn> getLegalMoves(String file, int rank) throws IllegalArgumentException, IllegalStateException;

    void promotePawn(String file, int rank, ChessPieceType type) throws IllegalArgumentException, IllegalStateException;
}
