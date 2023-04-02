package dovbenko.hw2.tsk1;

public class BusyCellException extends Exception {

    public BusyCellException(String message) {
        super(message);
    }

    public BusyCellException() {
        super("busy cell");
    }
}
