package dovbenko.hw3.tsk1.generator;

import dovbenko.hw3.tsk1.initializer.Initializer;
import dovbenko.hw3.tsk1.pier.PierLoader;
import dovbenko.hw3.tsk1.ships.Ship;
import dovbenko.hw3.tsk1.ships.Size;
import dovbenko.hw3.tsk1.ships.Type;

import java.util.ArrayList;
import java.util.Random;

public final class ShipsGenerator {

    private final Initializer initializer;

    public ShipsGenerator(Initializer initializer) {
        this.initializer = initializer;
    }

    private Type getRandomType() {
        Random random = new Random();
        return Type.values()[random.nextInt(Type.values().length)];
    }

    private Size getRandomSize() {
        Random random = new Random();
        return Size.values()[random.nextInt(Size.values().length)];
    }

    private Ship createShip(int shipNumber) {
        Type shipType = getRandomType();
        PierLoader pierLoader = initializer.getPierLoaders().get(shipType);
        pierLoader.increaseNeedToLoadShips();
        return new Ship(getRandomSize(),
                shipType,
                initializer.getTunnel(),
                shipNumber,
                pierLoader);

    }

    public ArrayList<Ship> generateShips(int shipsQuantity) {
        ArrayList<Ship> result = new ArrayList<>();
        for (int i = 0; i < shipsQuantity; i++) {

            result.add(createShip(i));
        }
        return  result;
    }
}
