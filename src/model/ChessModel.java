package model;

import java.util.*;

import static model.ChessPieceColor.*;
import static model.ChessPieceType.*;

public interface ChessModel<K> {

    // todo see if these types should be a part of the interface or i need to introduce more vars like K
    Set<ChessPieceType> PROMOTION_OPTIONS = new HashSet<>(Arrays.asList(ROOK, QUEEN, BISHOP, KNIGHT));

    Map<ChessPieceColor, Integer> INITIAL_VALUES = new HashMap<>() {{
        put(WHITE, 38); // todo see if i can generalize this
        put(BLACK, 38);
    }};

    K getPieceAt(int rank, int file);

    void removePiece(int rank, int file) throws IllegalArgumentException, IllegalStateException;

    void move(int rankFrom, int fileFrom, int rankTo, int fileTo) throws IllegalArgumentException, IllegalStateException;

    Set<Posn> getPreMoves(int rank, int file) throws IllegalArgumentException, IllegalStateException;

    Set<Posn> getLegalMoves(int rank, int file) throws IllegalArgumentException, IllegalStateException;

    void promotePawn(int rank, int file, ChessPieceType type) throws IllegalArgumentException, IllegalStateException;

    GameStatus getStatus();

    Map<ChessPieceColor, Integer> getValueCounts();
}
