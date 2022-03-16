import model.*;
import model.pieces.AChessPiece;

import java.util.Set;

import static model.ClassicChessUtils.*;

public final class Runner {

    public static void main(String[] args) {

        ReadableChessModel<AChessPiece> model = new ReadableClassicChess();

        System.out.println(model);
        Set<Posn> set = model.getLegalMoves("b", 2);
        System.out.println(setToBoardString(set));
        model.move("b", 2, "b", 4);

        System.out.println(model);
        set = model.getLegalMoves("b", 4);
        System.out.println(setToBoardString(set));

    }
}
