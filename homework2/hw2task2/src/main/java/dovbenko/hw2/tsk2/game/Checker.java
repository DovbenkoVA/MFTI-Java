package dovbenko.hw2.tsk2.game;

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
