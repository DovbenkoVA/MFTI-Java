package dovbenko.hw2.tsk2.game;

import java.util.ArrayList;

/**
 * The class of the checkers tower.
 *
 * @author Vladislav Dovbenko
 * @version 1.0
 */
public final class Tower {
    private final ArrayList<Checker> checkers = new ArrayList<>();

    public Tower(String composition) {
        char[] members = composition.toCharArray();
        for (char member : members) {
            switch (member) {
                case 'b':
                    checkers.add(new Checker(Color.BLACK, false));
                    break;
                case 'B':
                    checkers.add(new Checker(Color.BLACK, true));
                    break;
                case 'w':
                    checkers.add(new Checker(Color.WHITE, false));
                    break;
                case 'W':
                    checkers.add(new Checker(Color.WHITE, true));
                    break;
            }
        }
    }

    public ArrayList<Checker> getCheckers() {
        return checkers;
    }

    /**
     * Returns a string representation of the tower content
     *
     * @return string representation
     */
    public String getComposition() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Checker checker : getCheckers()) {
            Color color = checker.getColor();
            Boolean isKing = checker.getKing();
            if (color == Color.BLACK && isKing) {
                stringBuilder.append("B");
            } else if (color == Color.BLACK) {
                stringBuilder.append("b");
            } else if (color == Color.WHITE && isKing) {
                stringBuilder.append("W");
            } else if (color == Color.WHITE) {
                stringBuilder.append("w");
            }

        }
        return stringBuilder.toString();
    }

    /**
     * Performs the movement of the tower on the board with the check of the installation of the king
     *
     * @param oldPosition {@link BoardCell} cell of the initial position of the tower
     * @param newPosition {@link BoardCell} cell of the final position of the tower
     */
    public void move(BoardCell oldPosition, BoardCell newPosition) {
        changePosition(oldPosition, newPosition);
        checkSetIsKing(newPosition);
    }

    /**
     * Performs the hitting tower of the checkers
     *
     * @param oldPosition {@link BoardCell} cell of the initial position of the tower
     * @param newPosition {@link BoardCell} cell of the final position of the tower
     * @param targetCell  {@link BoardCell} cell of the position of the tower to hit
     */
    public void hit(BoardCell oldPosition, BoardCell newPosition, BoardCell targetCell) {

        Tower targetTower = targetCell.getTower();

        moveChecker(targetTower, this);
        if (targetTower.getCheckers().isEmpty()) {
            targetCell.setTower(null);
        }
        changePosition(oldPosition, newPosition);
        checkSetIsKing(newPosition);
    }

    private void moveChecker(Tower startTower, Tower finishTower) {
        Checker checker = startTower.getTopChecker();
        if (checker != null) {
            finishTower.getCheckers().add(checker);
            startTower.getCheckers().remove(checker);
        }
    }

    private void changePosition(BoardCell oldPosition, BoardCell newPosition) {
        newPosition.setTower(this);
        oldPosition.removeTower();

    }

    public Boolean isKing() {
        Boolean result = false;
        Checker topChecker = getTopChecker();
        if (topChecker != null) {
            result = topChecker.getKing();
        }
        return result;
    }

    public Color getColor() {
        Color result = null;
        Checker topChecker = getTopChecker();
        if (topChecker != null) {
            result = topChecker.getColor();
        }
        return result;
    }

    /**
     * Checks the position of the checker and sets the "king" sign if necessary.
     *
     * @param checkerPosition {@link BoardCell} cell of the checking position of the tower
     */
    public void checkSetIsKing(BoardCell checkerPosition) {
        int checkerRow = checkerPosition.getRowNumber();
        int boardSize = Board.getSize();
        Checker topChecker = getTopChecker();
        if (topChecker != null) {
            Color checkerColor = topChecker.getColor();
            boolean isGettingFinishRow = checkerRow == (boardSize - 1) && checkerColor == Color.WHITE
                    || checkerRow == 0 && checkerColor == Color.BLACK;
            if (isGettingFinishRow && !topChecker.getKing()) {
                topChecker.setKing(true);
            }
        }

    }

    private Checker getTopChecker() {
        Checker result = null;
        ArrayList<Checker> currentCheckers = getCheckers();
        if (!currentCheckers.isEmpty()) {
            result = currentCheckers.get(0);
        }
        return result;
    }

}
