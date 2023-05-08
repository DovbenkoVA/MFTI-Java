package dovbenko.hw3.tsk2.ringprocessor;

import dovbenko.hw3.tsk2.datapackage.DataPackage;
import dovbenko.hw3.tsk2.datastorage.DataStorage;
import dovbenko.hw3.tsk2.logger.LoggerFormatter;
import dovbenko.hw3.tsk2.node.Node;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.Handler;

/**
 * В конструкторе кольцо инициализируется, то есть создаются все узлы и данные на узлах.
 * В методе {@link RingProcessor#startProcessing()} запускается работа кольца - данные начинают
 * обрабатываться по часовой стрелке. Также производится логгирование в {@link RingProcessor#logs}.
 * Вся работа должна быть потокобезопасной и с обработкой всех возможных исключений. Если необходимо,
 * разрешается создавать собственные классы исключений.
 */
public class RingProcessor {

    private final int nodesAmount;
    private final int dataAmount;
    private final int processorPermits;
    private final File logs;

    private List<Node> nodeList;

    private final Logger logger;

    /**
     * Сюда идёт запись времени прохода каждого пакета данных.
     * Используется в {@link RingProcessor#averageTime()} для подсчета среднего времени
     * прохода данных к координатору.
     */

    List<Long> timeList;

    public RingProcessor(int nodesAmount, int dataAmount, int processorPermits, String logFileName) throws InterruptedException, IOException
    {
        this.nodesAmount = nodesAmount;
        this.dataAmount = dataAmount;
        this.logs = new File(logFileName);
        this.processorPermits = processorPermits;
        logger = Logger.getLogger(RingProcessor.class.getName());
        Handler fileHandler = new FileHandler(logFileName);
        fileHandler.setFormatter(new LoggerFormatter());
        logger.addHandler(fileHandler);

        init();
    }

    private void setCoordinator() {
        Random random = new Random();
        Node coordinator = this.nodeList.get(random.nextInt(this.nodesAmount));
        for (Node node : this.nodeList) {
            node.setCoordinator(coordinator);
            if (node == coordinator) {
                DataStorage dataStorage = new DataStorage(this.dataAmount);
                node.setDataStorage(dataStorage);
                logger.info(String.format("Data storage is installed %s to %s",
                        dataStorage, dataStorage.getDELIVERED_DATA_SiZE()));
            }

        }
    }

    private void setDataPackages() throws InterruptedException{
        for (int i = 0; i < this.dataAmount; i++) {
            Random random = new Random();
            Node destinationNode = this.nodeList.get(random.nextInt(this.nodesAmount));
            DataPackage dataPackage = new DataPackage(destinationNode, UUID.randomUUID().toString());
            Node node = this.nodeList.get(random.nextInt(this.nodesAmount));
            node.getBuffer().put(dataPackage);
        }
    }

    // Считается среднее время прохода.
    private long averageTime() {
        return 0;
    }

    private void init() throws InterruptedException{
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

        setCoordinator();
        setDataPackages();
        for (Node node: nodeList) {
            logger.info(""+node);
        }
        logger.info("Node creation completed");

    }

    public void startProcessing() {
        int cores = Runtime.getRuntime().availableProcessors();
        ArrayList<Future> submits = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(cores);
        for (Node node: nodeList) {
            Future sub = service.submit(node);
            submits.add(sub);
        }
        service.shutdown();

//        logger.info("All submits done!");

    }
}
