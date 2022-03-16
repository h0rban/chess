import model.ClassicChessUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

public class ClassicChessUtilsTest {

    @Test
    public void testGetNChars() {
        assertEquals(Collections.singletonList('a'), ClassicChessUtils.getNChars(1));
        assertEquals(ClassicChessUtils.getNChars(2), Arrays.asList('a', 'b'));
    }
}