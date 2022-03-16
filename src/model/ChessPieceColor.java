package model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public enum ChessPieceColor {

    WHITE, BLACK;

    private static final Map<ChessPieceColor, ChessPieceColor> nextMap = new HashMap<>() {{
        put(WHITE, BLACK);
        put(BLACK, WHITE);
    }};

    public int getColorIndex() {
        return Arrays.asList(ChessPieceColor.values()).indexOf(this);
    }

    public ChessPieceColor next() {
        return nextMap.get(this);
    }
}
