package dovbenko.hw2.tsk1;

public class InvalidMoveException extends Exception {
    public InvalidMoveException(String message) {
        super(message);
    }

    public InvalidMoveException() {
        super("invalid move");
    }
}
