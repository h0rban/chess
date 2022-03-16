package model;


import model.pieces.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ClassicChessUtils {

    public static final int SIZE = 8;
    public static final Map<Integer, String> INDEX2FILE = getFileMap();
    public static final Map<String, Integer> FILE2INDEX = getReverseFileMap();

    public static boolean notInRange(int rank, int file) {
        return rank < 0 || rank >= SIZE || file < 0 || file >= SIZE;
    }

    public static boolean inRange(int rank, int file) {
        return !notInRange(rank, file);
    }

    public static void checkRange(int rank, int file) {
        if (notInRange(rank, file)) {
            throw new IllegalArgumentException(String.format("tried to index out of bound: (%d, %d)", rank, file));
        }
    }

    public static void addToSetIfInRange(Set<Posn> set, int rank, int file) {
        if (inRange(rank, file)) {
            set.add(new Posn(rank, file));
        }
    }


    public static AChessPiece[] getPawnRow(ChessPieceColor color) {
        if (color == null) {
            throw new IllegalArgumentException();
        }
        AChessPiece[] row = new AChessPiece[SIZE];
        for (int i = 0; i < SIZE; i++) {
            row[i] = APawn.getPawnFromColor(color);
        }
        return row;
    }

    public static AChessPiece[] getPieceRow(ChessPieceColor color) throws IllegalArgumentException {
        if (color == null) {
            throw new IllegalArgumentException("expecting a valid color");
        }
        AChessPiece[] row = new AChessPiece[SIZE];
        List<AChessPiece> leftTwoRight = Arrays.asList(new Rook(color), new Knight(color), new Bishop(color));
        for (int i = 0; i < leftTwoRight.size(); i++) {
            row[i] = leftTwoRight.get(i);
            row[SIZE - i - 1] = leftTwoRight.get(i);
        }
        row[SIZE / 2 - 1] = new Queen(color);
        row[SIZE / 2] = new King(color);
        return row;
    }

    public static String pieceToString(AChessPiece p) {
        return p == null ? " " : p.toString();
    }


    public static String getValidFileChar(int file) {
        if (!INDEX2FILE.containsKey(file)) {
            throw new IllegalArgumentException();
        }
        return INDEX2FILE.get(file);
    }

    public static int getValidFileIndex(String file) {
        if (!FILE2INDEX.containsKey(file)) {
            throw new IllegalArgumentException();
        }
        return FILE2INDEX.get(file);
    }

    public static List<Character> getNChars(int n) {
        return getNChars(n, 97);
    }

    public static List<Character> getNChars(int n, int asciiStart) {
        if (n < 1 | n > 25) {
            throw new IllegalArgumentException("expecting n∈[1, 25]");
        }
        return IntStream.range(0, n).mapToObj(i -> (char) (asciiStart + i)).collect(Collectors.toList());
    }

    public static Map<Integer, String> getFileMap() {
        Map<Integer, String> map = new LinkedHashMap<>();
        List<Character> chars = getNChars(SIZE);
        for (int i = 0; i < SIZE; i++) {
            map.put(i, chars.get(i).toString());
        }
        return map;
    }

    public static Map<String, Integer> getReverseFileMap() {
        Map<String, Integer> map = new LinkedHashMap<>();
        List<Character> chars = getNChars(SIZE);
        for (int i = 0; i < SIZE; i++) {
            map.put(chars.get(i).toString(), i);
        }
        return map;
    }

    public static Set<String> setToBoardString(Set<Posn> set) {
        return set.stream().map(Posn::toBoardString).collect(Collectors.toSet());
    }

    public static AChessPiece[][] buildBoard() {
        AChessPiece[][] board = new AChessPiece[SIZE][SIZE];
        board[0] = getPieceRow(ChessPieceColor.WHITE);
        board[1] = getPawnRow(ChessPieceColor.WHITE);
        for (int rank = 2; rank < SIZE - 2; rank++) {
            board[rank] = new AChessPiece[SIZE];
        }
        board[SIZE - 2] = getPawnRow(ChessPieceColor.BLACK);
        board[SIZE - 1] = getPieceRow(ChessPieceColor.BLACK);
        return board;
    }

    public static Map<ChessPieceColor, Posn> buildKingsMap() {
        Map<ChessPieceColor, Posn> kings = new HashMap<>();
        kings.put(ChessPieceColor.WHITE, new Posn(0, 4));
        kings.put(ChessPieceColor.BLACK, new Posn(SIZE - 1, 4));
        return kings;
    }
}
