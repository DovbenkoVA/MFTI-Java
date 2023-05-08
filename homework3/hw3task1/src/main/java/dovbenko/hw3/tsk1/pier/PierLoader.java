package dovbenko.hw3.tsk1.pier;

import dovbenko.hw3.tsk1.initializer.Initializer;
import dovbenko.hw3.tsk1.ships.Ship;
import dovbenko.hw3.tsk1.ships.Type;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * A class describing the operation of the loader on the pier
 *
 * @version 1.0
 * @author Vladislav Dovbenko
 */
public final class PierLoader implements Runnable {

    private static final int GOODS_PORTION = 10;
    private static final int TIME_PORTION = 1000;
    private final ArrayBlockingQueue<Ship> loadingQueue;
    private final Type shipType;
    private int needToLoadShips;
    private int loadedShips;

    public PierLoader(Type shipType, int loadingQueueSize) {
        this.loadingQueue = new ArrayBlockingQueue<>(loadingQueueSize);
        this.shipType = shipType;

    }

    public void increaseNeedToLoadShips() {
        this.needToLoadShips++;
    }

    public ArrayBlockingQueue<Ship> getLoadingQueue() {
        return loadingQueue;
    }

    @Override
    public String toString() {
        return "PierLoader{"
                + "shipType=" + shipType
                + " needToLoadShips=" + needToLoadShips
                + " loadedShips=" + loadedShips + '}';
    }

    /**
     * A ship is removed from the queue corresponding to the type of goods,
     * a time delay is performed in accordance with the size of the ship, the state of the ship is recorded.
     */
    @Override
    public void run() {
        try {
            Logger logger = Logger.getLogger(Initializer.class.getName());
            while (needToLoadShips > 0 || !loadingQueue.isEmpty()) {
                Ship ship = loadingQueue.take();

                logger.info(String.format("%s : start loading %s", this, ship));
                int sleepTime = ship.getCargoQuantity() / GOODS_PORTION * TIME_PORTION;
                Thread.sleep(sleepTime);
                ship.setLoaded(true);
                loadedShips++;
                needToLoadShips--;
                logger.info(String.format("%s : loaded %s", this, ship));

            }
            logger.info(String.format("%s has finished its work", this));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
