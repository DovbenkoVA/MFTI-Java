package dovbenko.hw2.tsk1.game;

public final class BoardCell {
    private final Board board;
    private final String name;
    private final int rowNumber;
    private final int columnNumber;
    private final Color color;
    private Checker checker;

    public BoardCell(Board board, String name, int rowNumber, int columnNumber, Color color) {
        this.board = board;
        this.name = name;
        this.rowNumber = rowNumber;
        this.columnNumber = columnNumber;
        this.color = color;
    }

    public Color getColor() {
        return color;
    }

    public void setChecker(Checker checker) {
        this.checker = checker;
    }

    public int getRowNumber() {
        return rowNumber;
    }

    public int getColumnNumber() {
        return columnNumber;
    }

    public void removeChecker() {
        this.checker = null;
    }

    public Checker getChecker() {
        return checker;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BoardCell{")
                .append("board=").append(board)
                .append(", name=").append(name)
                .append(", rowNumber=").append(rowNumber)
                .append(", columnNumber=").append(columnNumber)
                .append("}");
        return stringBuilder.toString();
    }


}
