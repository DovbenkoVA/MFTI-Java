package dovbenko.hw2.tsk1;

public class GeneralErrorException extends Exception {
    public GeneralErrorException(String message) {
        super(message);
    }

    public GeneralErrorException() {
        super("general error");
    }
}
