package dovbenko.hw3.tsk2.node;

import dovbenko.hw3.tsk2.datapackage.DataPackage;
import dovbenko.hw3.tsk2.datastorage.DataStorage;
import dovbenko.hw3.tsk2.ringprocessor.RingProcessor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public final class Node implements Runnable {
    private static final long PROCESSING_TIME = 1L;
    private final int nodeId;
    private Node nextNode;
    private Node coordinator;
    private final Semaphore semaphore;
    private final ArrayBlockingQueue<DataPackage> bufferStack;
    private DataStorage dataStorage;

    public Node(int nodeId, int dataPackageNumber, Semaphore processor) {
        this.nodeId = nodeId;
        this.bufferStack = new ArrayBlockingQueue<>(dataPackageNumber, true);
        this.semaphore = processor;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNextNode(Node nextNode) {
        this.nextNode = nextNode;
    }

    public Node getNextNode() {
        return nextNode;
    }

    public void setCoordinator(Node coordinator) {
        this.coordinator = coordinator;
    }

    public static long getProcessingTime() {
        return PROCESSING_TIME;

    }

    public Node getCoordinator() {
        return coordinator;
    }

    public ArrayBlockingQueue<DataPackage> getBuffer() {
        return this.bufferStack;
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    public void setDataStorage(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public String toString() {
        return "Node{"
                + "nodeId=" + nodeId
                + ", nextNode=" + nextNode.getNodeId()
                + ", coordinator=" + coordinator.getNodeId()
                + ", bufferStack=" + bufferStack
                + '}';
    }

    @Override
    public void run() {
        Logger logger = Logger.getLogger(RingProcessor.class.getName());
        DataStorage coordinatorDataStorage = this.coordinator.getDataStorage();
        try {
            while (!coordinatorDataStorage.allDataDelivered()) {
                if (!bufferStack.isEmpty()) {
                    DataPackage dataPackage = bufferStack.take();

                    sleep(Node.getProcessingTime());
                    if (dataPackage.getDestinationNode() == this) {
                        coordinatorDataStorage.getDeliveredData().put(dataPackage);
                        dataPackage.setEndTime(System.nanoTime());
                        logger.info(String.format("node: %s Package %s has reached its destination",
                                this.nodeId, dataPackage));
                        logger.info(String.format("Node: %s Completed: %d / %d Buffer: %s",
                                this.nodeId,
                                coordinatorDataStorage.getDeliveredDataSize(),
                                coordinatorDataStorage.getDataRingCapacity(),
                                this.bufferStack));

                    } else {
                        nextNode.getBuffer().put(dataPackage);
                        logger.info(String.format("Move node: %s --> %s Package %s ",
                                this.nodeId, this.nextNode.getNodeId(), dataPackage));
                    }
                } else {
                    sleep(Node.getProcessingTime());

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
