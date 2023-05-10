package dovbenko.hw3.tsk2.datastorage;

import dovbenko.hw3.tsk2.datapackage.DataPackage;

import java.util.concurrent.ArrayBlockingQueue;

public final class DataStorage {
    private final int dataRingCapacity;
    private final ArrayBlockingQueue<DataPackage> deliveredData;

    public DataStorage(int dataRingCapacity) {
        this.dataRingCapacity = dataRingCapacity;
        this.deliveredData = new ArrayBlockingQueue<>(dataRingCapacity);
    }

    public synchronized boolean allDataDelivered() {
        return deliveredData.size() == dataRingCapacity;
    }

    public ArrayBlockingQueue<DataPackage> getDeliveredData() {
         return deliveredData;
    }

    public synchronized int getDataRingCapacity() {
        return dataRingCapacity;
    }

    public synchronized int getDeliveredDataSize() {
        return deliveredData.size();
    }

}
