package dovbenko.hw3.tsk2.datastorage;

import dovbenko.hw3.tsk2.datapackage.DataPackage;

import java.util.concurrent.ArrayBlockingQueue;

public class DataStorage {
    private final int DATA_RING_CAPACITY;
    private final ArrayBlockingQueue<DataPackage> DELIVERED_DATA;

    public DataStorage(int DATA_RING_CAPACITY) {
        this.DATA_RING_CAPACITY = DATA_RING_CAPACITY;
        this.DELIVERED_DATA = new ArrayBlockingQueue<DataPackage>(DATA_RING_CAPACITY);
    }

    public synchronized boolean allDataDelivered() {
        return DELIVERED_DATA.size() == DATA_RING_CAPACITY;
    }

    public ArrayBlockingQueue<DataPackage> getDELIVERED_DATA() {
        return DELIVERED_DATA;
    }

    public int getDATA_RING_CAPACITY() {
        return DATA_RING_CAPACITY;
    }

    public int getDELIVERED_DATA_SiZE() {
        return DELIVERED_DATA.size();
    }
}
