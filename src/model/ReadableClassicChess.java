package model;

import model.pieces.AChessPiece;

import java.util.Set;

import static model.ClassicChessUtils.getValidFileIndex;

public class ReadableClassicChess extends ClassicChess implements ReadableChessModel<AChessPiece> {

    private int fixRow(int rank) {
        return rank - 1;
    }

    private int fixFile(String file) {
        return getValidFileIndex(file);
    }

    @Override
    public AChessPiece getPieceAt(String file, int rank) {
        return getPieceAt(fixRow(rank), fixFile(file));
    }


    @Override
    public void removePiece(String file, int rank) throws IllegalArgumentException, IllegalStateException {
        removePiece(fixRow(rank), fixFile(file));
    }

    @Override
    public void move(String fileFrom, int rankFrom, String fileTo, int rankTo) throws IllegalArgumentException, IllegalStateException {
        move(fixRow(rankFrom), fixFile(fileFrom), fixRow(rankTo), fixFile(fileTo));
    }

    @Override
    public void promotePawn(String file, int rank, ChessPieceType type) throws IllegalArgumentException, IllegalStateException {
        promotePawn(fixRow(rank), fixFile(file), type);
    }

    @Override
    public Set<Posn> getPreMoves(String file, int rank) throws IllegalArgumentException, IllegalStateException {
        return getPreMoves(fixRow(rank), fixFile(file));
    }

    @Override
    public Set<Posn> getLegalMoves(String file, int rank) throws IllegalArgumentException, IllegalStateException {
        return getLegalMoves(fixRow(rank), fixFile(file));
    }
}
