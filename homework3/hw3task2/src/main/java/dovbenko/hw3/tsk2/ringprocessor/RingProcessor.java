package dovbenko.hw3.tsk2.ringprocessor;

import dovbenko.hw3.tsk2.datapackage.DataPackage;
import dovbenko.hw3.tsk2.datastorage.DataStorage;
import dovbenko.hw3.tsk2.logger.LoggerFormatter;
import dovbenko.hw3.tsk2.node.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutionException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Handler;

public final class RingProcessor {
    public static final int EXECUTION_WAITING_TIME = 300;
    private final int nodesAmount;
    private final int dataAmount;
    private final int processorPermits;
    private final DataStorage dataStorage;

    private List<Node> nodeList;

    private final Logger logger;

    public RingProcessor(int nodesAmount, int dataAmount, int processorPermits, String logFileName)
            throws InterruptedException, IOException {
        this.nodesAmount = nodesAmount;
        this.dataAmount = dataAmount;
        this.processorPermits = processorPermits;
        logger = Logger.getLogger(RingProcessor.class.getName());
        Handler fileHandler = new FileHandler(logFileName);
        fileHandler.setFormatter(new LoggerFormatter());
        logger.addHandler(fileHandler);
        this.dataStorage = new DataStorage(this.dataAmount);

        init();
    }

    public DataStorage getDataStorage() {
        return dataStorage;
    }

    private void setCoordinator(DataStorage coordinatorDataStorage) {
        Random random = new Random();
        Node coordinator = this.nodeList.get(random.nextInt(this.nodesAmount));
        for (Node node : this.nodeList) {
            node.setCoordinator(coordinator);
            if (node == coordinator) {
                node.setDataStorage(coordinatorDataStorage);
                logger.info(String.format("Data storage is installed %s to %s",
                        coordinatorDataStorage, coordinatorDataStorage.getDeliveredDataSize()));
            }

        }
    }

    private void setDataPackages() throws InterruptedException {
        for (int i = 0; i < this.dataAmount; i++) {
            Random random = new Random();
            Node destinationNode = this.nodeList.get(random.nextInt(this.nodesAmount));
            DataPackage dataPackage = new DataPackage(destinationNode, UUID.randomUUID().toString());
            Node node = this.nodeList.get(random.nextInt(this.nodesAmount));
            node.getBuffer().put(dataPackage);
        }
    }

    public long averageTime() {
        long result = 0;
        ArrayBlockingQueue<DataPackage> deliveredData = this.dataStorage.getDeliveredData();
        for (DataPackage dataPackage : deliveredData) {
            result += dataPackage.getEndTime() - dataPackage.getStartTime();
        }
        return result / dataAmount;
    }

    private void init() throws InterruptedException {
        logger.info("Node creation started");
        this.nodeList = new ArrayList<>();
        Semaphore processor = new Semaphore(this.processorPermits);
        for (int i = 0; i < this.nodesAmount; i++) {
            this.nodeList.add(new Node(i, this.dataAmount, processor));
        }

        for (int i = 0; i < this.nodesAmount - 1; i++) {
            nodeList.get(i).setNextNode(this.nodeList.get(i + 1));
        }
        nodeList.get(this.nodesAmount - 1).setNextNode(this.nodeList.get(0));

        setCoordinator(dataStorage);
        setDataPackages();
        for (Node node : nodeList) {
            logger.info("" + node);
        }
        logger.info("Node creation completed");

    }

    public void startProcessing() throws ExecutionException, InterruptedException {
        int cores = Runtime.getRuntime().availableProcessors();
        ArrayList<Future> submits = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(cores);

        for (Node node : nodeList) {
            Future sub = service.submit(node);
            submits.add(sub);

        }
        service.shutdown();

        while (!getDataStorage().allDataDelivered()) {
            Thread.sleep(EXECUTION_WAITING_TIME);
        }
        logger.info(String.format("Average time: %s ns", averageTime()));

    }
}
