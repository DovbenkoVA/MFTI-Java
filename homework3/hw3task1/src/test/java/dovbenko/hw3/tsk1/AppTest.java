package dovbenko.hw3.tsk1;

import dovbenko.hw3.tsk1.executor.Executor;
import dovbenko.hw3.tsk1.initializer.Initializer;
import dovbenko.hw3.tsk1.ships.Ship;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

public class AppTest {

    private static ArrayList<Ship> ships;
    @BeforeAll
    static void setUp() {
        ships = new ArrayList<>();
        try {
            Initializer initializer = new Initializer();
            ships = Executor.getResultLoadingShips(initializer);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    void initialLocationTest() {
        Assertions.assertThat(20).isEqualTo(ships.size());
    }

    @Test
    void allShipsHavePassedTunnelTest() {
        int loaded = 0;
        for (Ship ship: ships) {
            if (ship.isTunnelPassed()) {
                loaded++;
            }

        }
        Assertions.assertThat(20).isEqualTo(loaded);
    }
    @Test
    void allShipsLoadedTest() {
        int loaded = 0;
        for (Ship ship: ships) {
            if (ship.isLoaded()) {
                loaded++;
            }

        }
        Assertions.assertThat(20).isEqualTo(loaded);
    }

}
