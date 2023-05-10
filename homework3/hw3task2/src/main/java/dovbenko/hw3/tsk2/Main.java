package dovbenko.hw3.tsk2;

import dovbenko.hw3.tsk2.ringprocessor.RingProcessor;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public final class Main {

    private static final int NODES_AMOUNT = 10;
    private static final int DATA_AMOUNT = 20;
    private static final int PROCESSOR_PERMITS = 3;

    private Main() {

    }

    public static RingProcessor startProcessing(
            int nodesAmount, int dataAmount, int processorPermits, String logFileName)
            throws IOException, InterruptedException, ExecutionException {

        RingProcessor processor = new RingProcessor(nodesAmount, dataAmount, processorPermits, logFileName);
        processor.startProcessing();

        return processor;
    }

    public static void main(String[] args) {
        try {
            startProcessing(NODES_AMOUNT, DATA_AMOUNT, PROCESSOR_PERMITS, "logger.log");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
