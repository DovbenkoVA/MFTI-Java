package dovbenko.hw2.tsk2;

import org.junit.jupiter.api.Test;
import org.assertj.core.api.Assertions;

public class AppTest {
    @Test
    void initialLocationTest() {

        String expected = App.getFinalPosition(
                "a1_w c1_w e1_w f2_ww g5_wbb h2_w",
                "a3_b a5_bww b8_b c5_bwww d8_b e3_b e7_b f8_b g7_b h8_b",
                "");
        String actual = "a1_w c1_w e1_w f2_ww g5_wbb h2_w \n" +
                "a3_b a5_bww b8_b c5_bwww d8_b e3_b e7_b f8_b g7_b h8_b ";

        Assertions.assertThat(expected).isEqualTo(actual);
    }

    @Test
    void position0Test() {

        String expected = App.getFinalPosition(
                "a7_wbb b2_ww c1_w e1_w f2_w g1_w",
                "b4_bwww b8_b c3_b c7_b e5_bww e7_b f8_b g5_b g7_b h8_b",
                "b2_ww:d4_wwb:f6_wwbb:d8_wwbbb:b6_wwbbbb b4_bwww-a3_bwww");
        String actual = "a7_wbb b6_Wwbbbb c1_w e1_w e5_ww f2_w g1_w \n" +
                "a3_bwww b8_b f8_b g5_b g7_b h8_b ";

        Assertions.assertThat(expected).isEqualTo(actual);
    }

    @Test
    void position1Test() {

        String expected = App.getFinalPosition(
                "a1_w c1_w e1_w f2_ww h2_w g5_wbb",
                "a3_b e3_b a5_bww c5_bwww e7_b g7_b b8_b d8_b f8_b h8_b",
                "f2_ww:d4_wwb:b6_wwbb g7_b-f6_b h2_w-g3_w f6_b:h4_bw:f2_bww e1_w:g3_wb g5_bb-h4_bb");
        String actual = "invalid move";

        Assertions.assertThat(expected).isEqualTo(actual);
    }

    @Test
    void position2Test() {

        String expected = App.getFinalPosition(
                "a7_wbb b2_ww c1_w e1_w f2_w g1_w",
                "b4_bwww b8_b c3_b c7_b e5_bww e7_b f8_b g5_b g7_b h8_b",
                "b2_ww:d4_wwb:f6_wwbb:d8_wwbbb:b6_wwbbbb b4_bwww-a2_bwww");
        String actual = "white cell";

        Assertions.assertThat(expected).isEqualTo(actual);
    }

    @Test
    void position3Test() {

        String expected = App.getFinalPosition(
                "a7_wbb b2_ww c1_w e1_w f2_w g1_w",
                "b4_bwww b8_b c3_b c7_b e5_bww e7_b f8_b g5_b g7_b h8_b",
                "b2_ww:d4_wwb:f6_wwbb:d8_wwbbb:b6_wwbbbb b4_bwww-g5_bwww");
        String actual = "busy cell";

        Assertions.assertThat(expected).isEqualTo(actual);
    }

}
