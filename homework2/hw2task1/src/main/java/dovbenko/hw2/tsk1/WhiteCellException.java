package dovbenko.hw2.tsk1;

public class WhiteCellException extends Exception {
    public WhiteCellException(String message) {
        super(message);
    }

    public WhiteCellException() {
        super("white cell");
    }

}
