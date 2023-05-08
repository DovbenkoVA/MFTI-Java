package dovbenko.hw3.tsk1;

import dovbenko.hw3.tsk1.executor.Executor;
import dovbenko.hw3.tsk1.initializer.Initializer;
import dovbenko.hw3.tsk1.logger.LoggerFormatter;
import dovbenko.hw3.tsk1.ships.Ship;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public final class App {

    private App() {
    }

    public static void main(String[] args) {
        Initializer initializer = new Initializer();
        try {
            Logger logger = Logger.getLogger(Initializer.class.getName());
            Handler fileHandler = new FileHandler("logger.log");
            fileHandler.setFormatter(new LoggerFormatter());
            logger.addHandler(fileHandler);
            ArrayList<Ship> ships = Executor.getResultLoadingShips(initializer);
            System.out.println(ships);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
