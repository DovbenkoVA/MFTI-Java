package dovbenko.hw3.tsk2.node;

import dovbenko.hw3.tsk2.datapackage.DataPackage;
import dovbenko.hw3.tsk2.datastorage.DataStorage;
import dovbenko.hw3.tsk2.ringprocessor.RingProcessor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class Node implements Runnable {
    private static final long PROCESSING_TIME = 1L;
    private final int nodeId;
    private Node nextNode;
    private Node coordinator;
    private Boolean isCoordinator;
    private final Semaphore semaphore;
    private final ArrayBlockingQueue<DataPackage> bufferStack;
    private DataStorage dataStorage;

    public Node(int nodeId, int dataPackageNumber, Semaphore processor) {
        this.nodeId = nodeId;
        this.bufferStack = new ArrayBlockingQueue<>(dataPackageNumber);
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

    public void setCoordinator(Node coordinator) {
        this.coordinator = coordinator;
    }

    public Boolean getCoordinator() {
        return isCoordinator;
    }

    public void setCoordinator(Boolean coordinator) {
        isCoordinator = coordinator;
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
    public void run() {
        Logger logger = Logger.getLogger(RingProcessor.class.getName());
        try {
            while (!coordinator.getDataStorage().allDataDelivered() || !bufferStack.isEmpty()) {
                // semaphore.acquire();
                logger.info(String.format("Node: %s Completed: %d / %d Buffer: %s",
                        this.getNodeId(),
                        coordinator.getDataStorage().getDELIVERED_DATA_SiZE(),
                        coordinator.getDataStorage().getDATA_RING_CAPACITY(),
                        bufferStack));
                logger.info(String.format("Node: %s try take data", this.getNodeId()));
//                DataPackage dataPackage = bufferStack.take();
                DataPackage dataPackage = bufferStack.poll(1000L, TimeUnit.MILLISECONDS);
                logger.info(String.format("Node: %s get data", this.getNodeId()));

                sleep(PROCESSING_TIME);
                if (dataPackage.getDestinationNode() == this) {
                    coordinator.getDataStorage().getDELIVERED_DATA().put(dataPackage);
                    logger.info(String.format("node: %s Package %s has reached its destination",
                            this.getNodeId(), dataPackage));

                } else {
                    nextNode.getBuffer().put(dataPackage);
                    logger.info(String.format("Move node: %s --> %s Package %s ",
                            this.getNodeId(), nextNode.getNodeId(), dataPackage));
                }
                //semaphore.release();
                logger.info(String.format("Node: %s Buffer size: %s",
                        this.getNodeId(),
                        bufferStack.size()));

                logger.info(String.format("Data storage volume is %s", dataStorage.getDELIVERED_DATA_SiZE()));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String toString() {
        return "Node{" +
                "nodeId=" + nodeId +
                ", nextNode=" + nextNode.getNodeId() +
                ", coordinator=" + coordinator.getNodeId() +
//                ", semaphore=" + semaphore +
                ", bufferStack=" + bufferStack +
                '}';
    }
}
