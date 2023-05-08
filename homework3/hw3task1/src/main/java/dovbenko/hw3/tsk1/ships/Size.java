package dovbenko.hw3.tsk1.ships;

public enum Size {
    SMALL(10), MEDIUM(50), BIG(100);

    private final int size;

    Size(int size) {
        this.size = size;
    }

    public int getSize() {
        return this.size;
    }
}
