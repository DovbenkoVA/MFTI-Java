package dovbenko.hw3.tsk1.ships;

import dovbenko.hw3.tsk1.initializer.Initializer;
import dovbenko.hw3.tsk1.pier.PierLoader;

import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 * A class describing ships
 *
 * @version 1.0
 * @author Vladislav Dovbenko
 */
public final class Ship implements Runnable {

    private static final int TIME_IN_TUNNEL = 1000;
    private final Size size;
    private final Type type;
    private final int cargoQuantity;
    private final int shipNumber;
    private boolean isTunnelPassed;
    private boolean isLoaded;
    private final Semaphore tunnel;
    private final PierLoader pierLoader;

    public Ship(Size size, Type type, Semaphore tunnel, int shipNumber, PierLoader pierLoader) {
        this.size = size;
        this.type = type;
        this.tunnel = tunnel;
        this.shipNumber = shipNumber;
        this.pierLoader = pierLoader;
        this.cargoQuantity = this.size.getSize();
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public boolean isTunnelPassed() {
        return isTunnelPassed;
    }

    public void setLoaded(boolean loaded) {
        isLoaded = loaded;
    }

    public int getCargoQuantity() {
        return cargoQuantity;
    }

    @Override
    public String toString() {
        return "Ship{" + "size=" + size + ", type=" + type + ", shipNumber=" + shipNumber + '}';
    }

    /**
     * A method describing the movement of the ship through the tunnel to the pier.
     * After passing through the tunnel, the ship is placed in the loading queue.
     */
    @Override
    public void run() {
        Logger logger = Logger.getLogger(Initializer.class.getName());
        try {
            if (!isTunnelPassed) {
                tunnel.acquire();
                logger.info(String.format("%s arrived in the tunnel", this));
                sleep(TIME_IN_TUNNEL);
                isTunnelPassed = true;
                tunnel.release();
                logger.info(String.format("%s is taken from the tunnel", this));
            }
            pierLoader.getLoadingQueue().put(this);
            logger.info(String.format("%s awaiting loading in %s", this, pierLoader));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
