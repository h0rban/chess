package model;

import model.pieces.AChessPiece;

import java.util.*;
import java.util.stream.IntStream;

import static model.ChessPieceType.KING;
import static model.ClassicChessUtils.*;

public class Board {

    private final List<Move> moves;
    private final AChessPiece[][] board;
    private final Map<ChessPieceColor, Posn> kings;

    public Board() {
        this(buildBoard(), buildKingsMap(), new ArrayList<>());
    }

    public Board(Board board) {
        Board copy = board.copy();
        this.board = copy.board;
        this.kings = copy.kings;
        this.moves = copy.moves;
    }

    private Board(AChessPiece[][] board, Map<ChessPieceColor, Posn> kings, List<Move> moves) {
        this.board = board;
        this.kings = kings;
        this.moves = moves;
    }

    public Board copy() {
        // todo do i need to copy pieces
        AChessPiece[][] newBoard = new AChessPiece[SIZE][SIZE];
        parallelIterator((rank, file) -> newBoard[rank][file] = this.board[rank][file]);

        Map<ChessPieceColor, Posn> kingMap = new HashMap<>();
        for (Map.Entry<ChessPieceColor, Posn> e : this.kings.entrySet()) {
            kingMap.put(e.getKey(), new Posn(e.getValue()));
        }
        return new Board(newBoard, kingMap, new ArrayList<>(moves));
    }

    public AChessPiece getBoardPieceAt(int rank, int file) {
        checkRange(rank, file);
        return this.board[rank][file];
    }

    public AChessPiece getBoardPieceAt(Posn posn) {
        return getBoardPieceAt(posn.rank, posn.file);
    }

    public Board getBoardAfter(int rankFrom, int fileFrom, int rankTo, int fileTo) {
        Board board = copy();
        board.moveLegally(rankFrom, fileFrom, rankTo, fileTo);
        return board;
    }

    public void removeBoardPiece(int rank, int file) {
        board[rank][file] = null;
    }

    public void moveLegally(int rankFrom, int fileFrom, int rankTo, int fileTo) {
        AChessPiece p = getBoardPieceAt(rankFrom, fileFrom);
        board[rankTo][fileTo] = board[rankFrom][fileFrom];
        removeBoardPiece(rankFrom, fileFrom);

        if (p.getType() == KING) {
            kings.put(p.getColor(), new Posn(rankTo, fileTo));
        }
        moves.add(new Move(p, rankFrom, fileFrom, rankTo, fileTo));
    }

    public Set<Posn> getPiecePosns(ChessPieceColor color) {
        Set<Posn> set = new HashSet<>();
        parallelIterator((rank, file) -> {
            AChessPiece p = getBoardPieceAt(rank, file);
            if (p != null && p.getColor() == color) {
                set.add(new Posn(rank, file));
            }
        });
        return set;
    }

    public Map<ChessPieceType, Integer> getPieces(ChessPieceColor color) {
        Map<ChessPieceType, Integer> map = new HashMap<>();
        parallelIterator((rank, file) -> {
            AChessPiece p = getBoardPieceAt(rank, file);
            if (p != null && p.getColor() == color) {
                ChessPieceType type = p.getType();
                map.put(type, map.getOrDefault(type, 0) + 1);
            }
        });
        return map;
    }

    public boolean kingChecked(ChessPieceColor color) {
        Posn kingPosn = kings.get(color);
        return getPiecePosns(color.next())
                .stream()
                .parallel()
                .anyMatch(posn -> getBoardPieceAt(posn).getLegalMoves(posn, this).contains(kingPosn));
    }

    public boolean canEnPassant(int rankFrom, int fileTo) {
        if (moves.isEmpty()) {
            return false;
        }
        Move last = getLastMove();
        return last != null && last.pawnDouble && last.from.file == fileTo && Math.abs(rankFrom - last.from.rank) == 2;
    }

    private Move getLastMove() {
        if (moves.size() == 0) {
            return null;
        }
        return moves.get(moves.size() - 1);
    }

    public Posn getLastMoveTo() {
        Move last = getLastMove();
        if (last != null) {
            return last.to;
        }
        return null;
    }

    public boolean cellEmpty(int rank, int file) {
        return getBoardPieceAt(rank, file) == null;
    }

    public void setPiece(int rank, int file, AChessPiece piece) {
        this.board[rank][file] = piece;
    }

    public void parallelIterator(BoardIteratorFunction func) {
        IntStream.range(0, SIZE)
                .parallel()
                .forEach(rank -> IntStream.range(0, SIZE)
                        .parallel()
                        .forEach(file -> func.run(rank, file)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Board)) return false;
        AChessPiece[][] board1 = ((Board) o).board;
        return IntStream.range(0, SIZE)
                .parallel()
                .allMatch(rank -> IntStream.range(0, SIZE)
                        .parallel()
                        .allMatch(file -> board[rank][file] == board1[rank][file]));
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    interface BoardIteratorFunction {
        void run(int rank, int file);
    }
}
