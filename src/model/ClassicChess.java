package model;

import model.pieces.AChessPiece;

import java.util.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import static model.GameStatus.*;
import static model.ChessPieceColor.*;
import static model.ChessPieceType.*;
import static model.ClassicChessUtils.*;

public class ClassicChess implements ChessModel<AChessPiece> {

    // todo
    //  - add castling
    //  - have different constructors like from list of moves / maybe string text
    //  - check return by reference

    private final Board board;
    private final Map<Board, Integer> repetitionCounts;
    private final Map<ChessPieceColor, Integer> valueCounts;

    private GameStatus status;
    private ChessPieceColor colorToMove;
    private boolean timeForPromotion;

    public ClassicChess() {
        this.board = new Board();
        this.repetitionCounts = new HashMap<>();
        this.valueCounts = new HashMap<>(INITIAL_VALUES);

        this.colorToMove = WHITE;
        this.status = PLAYING;
        this.timeForPromotion = false;
    }

    @Override
    public AChessPiece getPieceAt(int rank, int file) {
        AChessPiece p = this.board.getBoardPieceAt(rank, file);
        if (p == null) {
            throw new IllegalArgumentException(String.format("There is no chess piece at (%d, %d)", rank, file));
        }
        return p;
    }


    @Override
    public void removePiece(int rank, int file) throws IllegalArgumentException, IllegalStateException {
        checkPlaying();
        AChessPiece p = board.getBoardPieceAt(rank, file);
        if (p != null) {
            this.board.removeBoardPiece(rank, file);
            valueCounts.put(p.getColor(), valueCounts.get(p.getColor()) - p.getType().getValue());
        }
    }

    @Override
    public void move(int rankFrom, int fileFrom, int rankTo, int fileTo) throws IllegalArgumentException, IllegalStateException {
        checkRange(rankTo, fileTo);
        AChessPiece p = getPieceAt(rankFrom, fileFrom);
        if (timeForPromotion) {
            throw new IllegalStateException("need to promote first before making a new move");
        }
        if (p.getColor() != colorToMove) {
            throw new IllegalArgumentException();
        }
        if (!getLegalMoves(rankFrom, fileFrom).contains(new Posn(rankTo, fileTo))) {
            throw new IllegalArgumentException();
        }
        moveLegally(rankFrom, fileFrom, rankTo, fileTo);
        if (!timeForPromotion) {
            updateGameStatus();
        }
    }

    @Override
    public void promotePawn(int rank, int file, ChessPieceType type) throws IllegalArgumentException, IllegalStateException {
        checkPlaying();
        if (!PROMOTION_OPTIONS.contains(type)) {
            throw new IllegalArgumentException();
        }
        AChessPiece p = getPieceAt(rank, file);
        if (!p.readyForPromotion(rank)) {
            throw new IllegalArgumentException();
        }

        board.setPiece(rank, file, type.pieceFromType(p.getColor()));
        timeForPromotion = false;
        updateGameStatus();
    }


    private void moveLegally(int rankFrom, int fileFrom, int rankTo, int fileTo) {

        AChessPiece p = getPieceAt(rankFrom, rankTo);

        if (p.getType() == PAWN && board.canEnPassant(rankFrom, fileTo)) {
            // todo check that this is working properly
            Posn lastTo = board.getLastMoveTo();
            removePiece(lastTo.rank, lastTo.file);
        }
        removePiece(rankTo, fileTo);
        board.moveLegally(rankFrom, fileFrom, rankTo, fileTo);
        if (p.readyForPromotion(rankTo)) {
            timeForPromotion = true;
        }
    }

    private void checkPlaying() throws IllegalStateException {
        if (status != PLAYING) {
            throw new IllegalArgumentException();
        }
    }

    private void updateGameStatus() {

        Map<ChessPieceType, Integer> countWhite = board.getPieces(WHITE);
        Map<ChessPieceType, Integer> countBlack = board.getPieces(BLACK);
        if (twoKnightsVsKing(countWhite, countBlack)
                || twoKnightsVsKing(countBlack, countWhite)
                || insufficientMaterial(countWhite) && insufficientMaterial(countBlack)) {
            status = DRAW_INSUFFICIENT_MATERIAL;
            return;
        }

        int repetitionCount = repetitionCounts.getOrDefault(board, 0) + 1;
        if (repetitionCount == 3) {
            status = DRAW_REPETITION;
            return;
        }

        boolean kingChecked = board.kingChecked(colorToMove.next());
        boolean noMoves = board.getPiecePosns(colorToMove.next())
                .stream()
                .parallel()
                .allMatch(posn -> getLegalMoves(posn.rank, posn.file).size() == 0);
        if (kingChecked && noMoves) {
            status = CHECKMATE;
            return;
        }
        if (!kingChecked && noMoves) {
            status = DRAW_STALEMATE;
            return;
        }

        if (status == PLAYING) {
            colorToMove = colorToMove.next();
            repetitionCounts.put(board, repetitionCount);
        }
    }

    private boolean twoKnightsVsKing(Map<ChessPieceType, Integer> a, Map<ChessPieceType, Integer> b) {
        return a.size() == 1 && b.size() == 2 && b.getOrDefault(KNIGHT, -1) == 2;
    }

    private boolean insufficientMaterial(Map<ChessPieceType, Integer> counter) {
        return counter.size() == 1
                || counter.size() == 2 && Stream.of(KNIGHT, BISHOP)
                .anyMatch(type -> counter.getOrDefault(type, 0) == 1);
    }

    @Override
    public Set<Posn> getPreMoves(int rank, int file) throws IllegalArgumentException, IllegalStateException {
        checkPlaying();
        return getPieceAt(rank, file).getPreMoves(rank, file);
    }
    @Override
    public Set<Posn> getLegalMoves(int rank, int file) throws IllegalArgumentException, IllegalStateException {
        checkPlaying();
        AChessPiece p = getPieceAt(rank, file);
        return p.getLegalMoves(rank, file, board)
                .stream()
                .parallel()
                .filter(posn -> !board.getBoardAfter(rank, file, posn.rank, posn.file).kingChecked(p.getColor()))
                .collect(Collectors.toCollection(HashSet::new));
    }

    @Override
    public GameStatus getStatus() {
        return status;
    }

    @Override
    public Map<ChessPieceColor, Integer> getValueCounts() {
        return new HashMap<>(valueCounts);
    }

    private String getSummary() {
        // todo move to controller later
        return ""
                + "game status:\t" + status + "\n"
                + "next move:\t\t" + colorToMove + "\n";
    }

    @Override
    public String toString() {

        StringBuilder fileIndexBuilder = new StringBuilder("    ");
        getNChars(SIZE).forEach(c -> fileIndexBuilder.append(c).append("   "));
        String fileIndex = fileIndexBuilder.delete(fileIndexBuilder.length() - 3, fileIndexBuilder.length()).toString();
        String vDash = "   ––– ––– ––– ––– ––– ––– ––– –––";

        StringBuilder out = new StringBuilder(fileIndex).append("\n");
        for (int rank = 0; rank < SIZE; rank++) {
            out.append(vDash).append("\n").append(rank + 1).append(" | ");
            for (int file = 0; file < SIZE; file++) {
                out.append(pieceToString(board.getBoardPieceAt(rank, file))).append(" | ");
            }
            out.append(rank + 1).append("\n");
        }
        return out.append(vDash).append("\n").append(fileIndex).append("\n") + "\n" + getSummary(); // todo move later
    }
}
