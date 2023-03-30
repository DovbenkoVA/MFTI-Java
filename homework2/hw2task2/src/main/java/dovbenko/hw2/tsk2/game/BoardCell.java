package dovbenko.hw2.tsk2.game;

public final class BoardCell {
    private final String name;
    private final int rowNumber;
    private final int columnNumber;
    private final Color color;
    private Tower tower;

    public BoardCell(String name, int rowNumber, int columnNumber, Color color) {
        this.name = name;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public Color getColor() {
        return color;
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public void removeTower() {
        this.tower = null;
    }
}
