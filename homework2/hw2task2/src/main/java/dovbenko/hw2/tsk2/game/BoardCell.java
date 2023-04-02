package dovbenko.hw2.tsk2.game;

/**
 * The class of cells of the checkers board
 *
 * @author Vladislav Dovbenko
 * @version 1.0
 */
public final class BoardCell {
    private final String name;
    private final int rowNumber;
    private final int columnNumber;
    private final Color color;
    private Tower tower;

    /**
     * Constructor of class objects
     *
     * @param name         name in the format a1, a2, ... , h8.
     * @param rowNumber    index of the row of the board cell
     * @param columnNumber index of the column of the board cell
     * @param color        color of the board cell
     */
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
