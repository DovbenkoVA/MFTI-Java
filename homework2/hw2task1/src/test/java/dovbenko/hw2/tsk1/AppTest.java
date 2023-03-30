package dovbenko.hw2.tsk1;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

import java.util.Locale;

public class AppTest {

    @Test
    void testPosition1() {
        String expected = App.getFinalPosition(
                "a1 a3 b2 c1 c3 d2 e1 e3 f2 g1 g3 h2",
                "a7 b6 b8 c7 d6 d8 e7 f6 f8 g7 h6 h8",
                "g3-f4 f6-e5 c3-d4 e5:c3 b2:d4 d6-c5 d2-c3 g7-f6 h2-g3 h8-g7 c1-b2 f6-g5 g3-h4 g7-f6 f4-e5 f8-g7"
                        .toLowerCase(Locale.ROOT));
        String actual = "a1 a3 b2 c3 d4 e1 e3 e5 f2 g1 h4 \n" +
                "a7 b6 b8 c5 c7 d8 e7 f6 g5 g7 h6 ";

        Assertions.assertThat(expected).isEqualTo(actual);
    }

    @Test
    void testPosition2() {
        String expected = App.getFinalPosition(
                "a1 d2 d4 f2",
                "b4 d6 f4",
                "a1-b2 b4-a3 d4-c5 a3:c1:e3:g1 c5:e7 G1-C5 e7-f8 C5-D4".toLowerCase(Locale.ROOT));
        String actual = "F8 \n" +
                "D4 f4 ";

        Assertions.assertThat(expected).isEqualTo(actual);
    }

    @Test
    void testPosition4() {
        String expected = App.getFinalPosition(
                "F8 b2 d4 f2",
                "b4 d6 f4",
                "F8:C5:A3 f4-g5 A3-B2 g5-f6 d4-c5 f6-e7".toLowerCase(Locale.ROOT));
        String actual = "busy cell";

        Assertions.assertThat(expected).isEqualTo(actual);
    }

    @Test
    void testPosition7() {
        String expected = App.getFinalPosition(
                "D8 c5 f6 g1",
                "a5 f4 h6 h8",
                "c5-d6 f4-e3 d6-c7 h8-g7 c7-b8 g7:e5 B8:F4:D2 h6-g5".toLowerCase(Locale.ROOT));
        String actual = "invalid move";

        Assertions.assertThat(expected).isEqualTo(actual);
    }

    @Test
    void testPosition11() {
        String expected = App.getFinalPosition(
                "F8 c3 c7 d6",
                "a5 a7 b6 b8 d8 f4",
                "c3-b4 a5:c3 c7:a5 c3-d2 F8-H6 d2-c1 H6:E3 C1:F4:C7".toLowerCase(Locale.ROOT));
        String actual = "a5 \n" +
                "C7 a7 b8 d8 ";

        Assertions.assertThat(expected).isEqualTo(actual);
    }
}
