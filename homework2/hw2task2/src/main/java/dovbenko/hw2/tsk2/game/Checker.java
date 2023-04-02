package dovbenko.hw2.tsk2.game;

/**
 * The class of the checkers
 *
 * @author Vladislav Dovbenko
 * @version 1.0
 */
public final class Checker {
    private final Color color;
    private Boolean isKing;

    public Checker(Color color, Boolean isKing) {
        this.color = color;
        this.isKing = isKing;

    }

    public Color getColor() {
        return color;
    }

    public Boolean getKing() {
        return isKing;
    }

    public void setKing(Boolean king) {
        isKing = king;
    }
}
