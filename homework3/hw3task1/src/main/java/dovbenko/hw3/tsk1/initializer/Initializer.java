package dovbenko.hw3.tsk1.initializer;

import dovbenko.hw3.tsk1.pier.PierLoader;
import dovbenko.hw3.tsk1.ships.Type;

import java.util.HashMap;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

public final class Initializer {

    private static final int DEFAULT_SHIPS_NUMBER = 20;
    private static final int MAX_SHIPS_IN_TUNNEL = 5;

    private final HashMap<Type, PierLoader> pierLoaders;
    private final Semaphore tunnel;
    private final int shipsNumber;
    private final Logger logger = Logger.getLogger(Initializer.class.getName());

    public Initializer() {

        this.tunnel = new Semaphore(MAX_SHIPS_IN_TUNNEL);
        this.shipsNumber = DEFAULT_SHIPS_NUMBER;

        this.pierLoaders = new HashMap<>();
        this.pierLoaders.put(Type.BREAD, new PierLoader(Type.BREAD, shipsNumber));
        this.pierLoaders.put(Type.BANANAS, new PierLoader(Type.BANANAS, shipsNumber));
        this.pierLoaders.put(Type.CLOTHES, new PierLoader(Type.CLOTHES, shipsNumber));

    }

    public HashMap<Type, PierLoader> getPierLoaders() {
        return pierLoaders;
    }

    public Semaphore getTunnel() {
        return tunnel;
    }

    public int getShipsNumber() {
        return shipsNumber;
    }
}
