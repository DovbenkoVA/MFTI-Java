package dovbenko.hw3.tsk2;

import dovbenko.hw3.tsk2.datapackage.DataPackage;
import dovbenko.hw3.tsk2.ringprocessor.RingProcessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;

public class MainTest {

    private static RingProcessor processor;

    @BeforeAll
    static void setUp() {
        try {
            processor = new RingProcessor(10, 20, 3, "testLog.log");
            processor.startProcessing();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    void dataAmountTest() {
        Assertions.assertThat(20).isEqualTo(processor.getDataStorage().getDeliveredDataSize());
    }

    @Test
    void averageTimeTest() {
        long equals = 0;
        ArrayBlockingQueue<DataPackage> deliveredData = processor.getDataStorage().getDeliveredData();
        for (DataPackage dataPackage : deliveredData) {
            equals += dataPackage.getEndTime() - dataPackage.getStartTime();
        }
        equals /= processor.getDataStorage().getDeliveredDataSize();

        Assertions.assertThat(processor.averageTime()).isEqualTo(equals);
    }

}
