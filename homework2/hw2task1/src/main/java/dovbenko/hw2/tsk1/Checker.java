package dovbenko.hw2.tsk1;

public final class Checker {
    private BoardCell boardCell;
    private final Color color;
    private Boolean isKing;

    public Checker(BoardCell boardCell, Color color, Boolean isKing) {
        this.boardCell = boardCell;
        this.color = color;
        this.isKing = isKing;

    }

    public void setCell(BoardCell cell) {
        this.boardCell = cell;
    }

    /**
     * Checks the position of the checker and sets the "king" sign if necessary.
     */
    public void checkSetIsKing() {
        BoardCell checkerPosition = this.getBoardCell();
        int checkerRow = checkerPosition.getRowNumber();
        int boardSize = Board.getSize();
        Color checkerColor = this.getColor();
        Boolean isGettingFinishRow = checkerRow == (boardSize - 1) && checkerColor == Color.WHITE
                || checkerRow == 0 && checkerColor == Color.BLACK;
        if (isGettingFinishRow && !this.isKing) {
            this.isKing = true;
        }
    }

    public BoardCell getBoardCell() {
        return boardCell;
    }

    public Color getColor() {
        return color;
    }

    public Boolean getIsKing() {
        return isKing;
    }

    public void move(BoardCell newPosition) {
        changePosition(newPosition);
        checkSetIsKing();
    }

    public void hit(Checker target, BoardCell newPosition) {
        BoardCell targetCell = target.getBoardCell();
        targetCell.removeChecker();
        target.clearCell();

        changePosition(newPosition);
    }

    public void clearCell() {
        this.boardCell = null;
    }

    private void changePosition(BoardCell newPosition) {
        BoardCell oldPosition = getBoardCell();
        oldPosition.removeChecker();
        this.clearCell();
        this.setCell(newPosition);
        newPosition.setChecker(this);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Checker{")
                .append("boardCell=")
                .append(boardCell)
                .append(", color=")
                .append(color)
                .append(", isKing=")
                .append(isKing).append("}");
        return stringBuilder.toString();
    }
}
