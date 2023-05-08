package dovbenko.hw3.tsk2;

import dovbenko.hw3.tsk2.ringprocessor.RingProcessor;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        try {
            RingProcessor processor = new RingProcessor(
                    10,
                    20,
                    3,
                    "logger.log");
            processor.startProcessing();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
