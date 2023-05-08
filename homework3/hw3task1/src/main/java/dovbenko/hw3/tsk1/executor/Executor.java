package dovbenko.hw3.tsk1.executor;

import dovbenko.hw3.tsk1.generator.ShipsGenerator;
import dovbenko.hw3.tsk1.initializer.Initializer;
import dovbenko.hw3.tsk1.pier.PierLoader;
import dovbenko.hw3.tsk1.ships.Ship;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

public final class Executor {
    public static final int EXECUTION_WAITING_TIME = 300;

    private static boolean allSubmitsDone(ArrayList<Future> submits) {
        boolean result = true;
        for (Future submit : submits) {
            if (!submit.isDone()) {
                result = false;
                break;
            }
        }
        return result;
    }

    private Executor() {
    }

    /**
     * The method that starts the execution of the script, after executing the method,
     * the state of the ships is returned after passing through the tunnel and loading.
     *
     * @param initializer Initializer of initial data
     * @return List of ships after passing through the tunnel and loading.
     * @throws InterruptedException
     */
    public static ArrayList<Ship> getResultLoadingShips(Initializer initializer) throws InterruptedException {

        Logger logger = Logger.getLogger(Initializer.class.getName());
        int cores = Runtime.getRuntime().availableProcessors();
        ShipsGenerator generator = new ShipsGenerator(initializer);
        ArrayList<Ship> ships = generator.generateShips(initializer.getShipsNumber());
        ArrayList<Future> submits = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(cores);
        for (Ship ship : ships) {
            Future sub = service.submit(ship);
            submits.add(sub);
        }

        for (PierLoader pierLoader : initializer.getPierLoaders().values()) {
            Future sub = service.submit(pierLoader);
            submits.add(sub);
        }

        service.shutdown();

        while (!allSubmitsDone(submits)) {
            Thread.sleep(EXECUTION_WAITING_TIME);

        }
        logger.info("All submits done!");
        return ships;
    }
}
