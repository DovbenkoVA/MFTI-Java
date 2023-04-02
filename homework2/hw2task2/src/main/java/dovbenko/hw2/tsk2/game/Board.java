package dovbenko.hw2.tsk2.game;

import dovbenko.hw2.tsk2.exception.BusyCellException;
import dovbenko.hw2.tsk2.exception.GeneralErrorException;
import dovbenko.hw2.tsk2.exception.InvalidMoveException;
import dovbenko.hw2.tsk2.exception.WhiteCellException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A class of the board for checkers and an interface for entering initial positions, moves and getting the final
 * position of checkers
 *
 * @author Vladislav Dovbenko
 * @version 1.0
 */
public final class Board {
    private static final Pattern POSITIONS_PATTERN = Pattern.compile("\\b([a-h][1-8])_([bBwW]+)");
    private static final char[] COORDINATE_SYMBOLS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private static final Pattern MOVEMENTS_PATTERN =
            Pattern.compile("([a-h][1-8]_([bBwW]+)-[a-h][1-8]_([bBwW]+))|[a-h][1-8]_[bBwW]+(:[a-h][1-8]_[bBwW]+)+");
    private static final Pattern MOVE_PATTERN = Pattern.compile("([a-h][1-8])_([bBwW]+)-([a-h][1-8])_([bBwW]+)");
    private static final Pattern HIT_PATTERN = Pattern.compile("([a-h][1-8])_([bBwW]+)(:([a-h][1-8])_([bBwW]+))+");
    private static final int SIZE = 8;

    private final HashMap<String, BoardCell> boardIndex;
    private String stringMovies;
    private final ArrayList<ArrayList<BoardCell>> cells;

    /**
     * Constructor of the instance. The array of cells and the index of cell search by name are automatically
     * initialized.
     */
    public Board() {
        this.cells = initializeNewCells();
        this.boardIndex = newBoardIndex();

    }

    private HashMap<String, BoardCell> newBoardIndex() {
        HashMap<String, BoardCell> result = new HashMap<>();
        for (ArrayList<BoardCell> row : this.cells) {
            for (BoardCell cell : row) {
                result.put(cell.getName(), cell);
            }
        }
        return result;
    }

    /**
     * Initializes the composition of the cells of the new checkers board
     *
     * @return table of the checkers {@link Board#cells}
     */
    private ArrayList<ArrayList<BoardCell>> initializeNewCells() {
        ArrayList<ArrayList<BoardCell>> result = new ArrayList<>();
        Color color = Color.BLACK;
        for (int rowNumber = 0; rowNumber < SIZE; rowNumber++) {
            ArrayList<BoardCell> row = new ArrayList<>();
            for (int columnNumber = 0; columnNumber < SIZE; columnNumber++) {
                String cellName = nameCellByCoordinates(rowNumber, columnNumber);
                row.add(new BoardCell(cellName, rowNumber, columnNumber, color));
                color = getOppositeColor(color);
            }
            result.add(row);
            color = getOppositeColor(color);
        }
        return result;
    }

    /**
     * Method of getting the name of the board cell by coordinates
     *
     * @param row    index of the row of the board cell
     * @param column index of the column of the board cell
     * @return cell name in the format a1, a2, ... , h8
     */
    public static String nameCellByCoordinates(int row, int column) {

        return String.valueOf(Board.COORDINATE_SYMBOLS[column]) + (row + 1);

    }

    /**
     * Method of getting the size of the board
     *
     * @return size of the board
     */
    public static int getSize() {
        return SIZE;

    }

    private Color getOppositeColor(Color color) {
        return (color == Color.BLACK) ? Color.WHITE : Color.BLACK;
    }

    /**
     * Method of getting the board cell by name
     *
     * @param cellName name of the board cell
     * @return board cell {@link BoardCell}
     */
    public BoardCell getCellByName(String cellName) {
        String searchKey = cellName.toLowerCase(Locale.ROOT);

        return boardIndex.get(searchKey);

    }

    /**
     * Method of getting the board cell by coordinates
     *
     * @param row    Index of the row of the board cell
     * @param column Index of the column of the board cell
     * @return board cell {@link BoardCell}
     */
    public BoardCell getCellByCoordinates(int row, int column) {
        BoardCell result = null;
        if (coordinatesIsCorrect(row, column)) {
            result = cells.get(row).get(column);
        }
        return result;
    }

    private boolean coordinatesIsCorrect(int row, int column) {
        return row >= 0 && row < Board.SIZE && column >= 0 && column < Board.SIZE;
    }

    /**
     * The method sets the initial position of the checkers on the board by a row of entries.
     *
     * @param stringPosition row of entries.
     */
    public void setStartingPosition(String stringPosition) {
        Matcher matcher = Board.POSITIONS_PATTERN.matcher(stringPosition);
        while (matcher.find()) {
            BoardCell boardCell = getCellByName(matcher.group(1));
            Tower tower = new Tower(matcher.group(2));
            boardCell.setTower(tower);

        }

    }

    public void setStringMovies(String stringMovies) {
        this.stringMovies = stringMovies;
    }

    /**
     * Returns the final position of the checkers on the board.
     *
     * @return a string containing the final arrangement of checkers on the board.
     */
    public String stringCheckersPositions() {
        HashMap<Color, ArrayList<String>> checkersPositions = getCheckersPositions();
        StringBuilder stringBuilder = new StringBuilder();
        for (String name : checkersPositions.get(Color.WHITE)) {
            stringBuilder.append(name).append(" ");
        }
        stringBuilder.append("\n");
        for (String name : checkersPositions.get(Color.BLACK)) {
            stringBuilder.append(name).append(" ");
        }
        return stringBuilder.toString();

    }

    /**
     * Returns the final state of checkers
     *
     * @return the structure of checkers on the board
     */
    private HashMap<Color, ArrayList<String>> getCheckersPositions() {
        HashMap<Color, ArrayList<String>> result = new HashMap<>();
        ArrayList<String> namesWhite = new ArrayList<>();
        ArrayList<String> namesBlack = new ArrayList<>();

        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                BoardCell cell = getCellByCoordinates(i, j);
                Tower tower = cell.getTower();
                if (tower != null) {
                    String cellName = cell.getName() + "_" + tower.getComposition();
                    Checker checker = tower.getCheckers().get(0);
                    switch (checker.getColor()) {
                        case BLACK:
                            namesBlack.add(cellName);
                            break;
                        case WHITE:
                            namesWhite.add(cellName);
                            break;
                    }
                }
            }
        }

        Collections.sort(namesBlack);
        Collections.sort(namesWhite);
        result.put(Color.WHITE, namesWhite);
        result.put(Color.BLACK, namesBlack);

        return result;

    }

    /**
     * The main method that performs the movement and taking of checkers.
     *
     * @throws BusyCellException     the target cell is occupied
     * @throws WhiteCellException    the target cell is white (checkers are placed only on black and, due to the rules,
     *                               cannot be on white)
     * @throws InvalidMoveException  it is mandatory to beat in checkers. Moreover, it is necessary to beat the whole
     *                               chain to the end. The error is displayed if the player has an option to beat the
     *                               checker, but he does not use it, but goes to another cell.
     * @throws GeneralErrorException other errors
     */
    public void makeMovementsAndHits()
            throws BusyCellException, GeneralErrorException, WhiteCellException, InvalidMoveException {
        Matcher matcher = Board.MOVEMENTS_PATTERN.matcher(this.stringMovies);
        while (matcher.find()) {
            String record = matcher.group();
            if (isMove(record)) {
                tryToMakeMove(matcher.group());
            } else if (isHit(record)) {
                tryToMakeHits(matcher.group());
            }
        }
    }

    private Boolean isMove(String record) {
        return Board.MOVE_PATTERN.matcher(record).find();
    }

    private Boolean isHit(String record) {
        return Board.HIT_PATTERN.matcher(record).find();
    }

    private void tryToMakeMove(String record)
            throws BusyCellException, GeneralErrorException, WhiteCellException, InvalidMoveException {
        Matcher matcher = POSITIONS_PATTERN.matcher(record);
        if (matcher.find()) {
            BoardCell startCell = getCellByName(matcher.group(1));
            checkStartCellToMove(startCell);

            if (matcher.find()) {
                BoardCell finishCell = getCellByName(matcher.group(1));

                checkFinishCellToMove(finishCell);
                makeMove(startCell, finishCell);
            }

        }

    }

    private void tryToMakeHits(String record)
            throws BusyCellException, GeneralErrorException, WhiteCellException {
        Matcher matcher = POSITIONS_PATTERN.matcher(record);
        if (matcher.find()) {
            BoardCell startCell = getCellByName(matcher.group(1));

            checkTowerExists(startCell);

            while (matcher.find()) {
                BoardCell nextCell = getCellByName(matcher.group(1));
                makeHit(startCell, nextCell);
                startCell = nextCell;
            }
        }

    }

    private void checkCellIsBlack(BoardCell cell) throws WhiteCellException {
        if (cell.getColor() == Color.WHITE) {
            throw new WhiteCellException();
        }
    }

    private void checkCellExists(BoardCell cell) throws GeneralErrorException {
        if (cell == null) {
            throw new GeneralErrorException();
        }
    }

    private void checkTowerExists(BoardCell cell) throws GeneralErrorException {
        if (cell.getTower() == null) {
            throw new GeneralErrorException();
        }
    }

    private void checkCellNotBusy(BoardCell cell) throws BusyCellException {
        if (cell.getTower() != null) {
            throw new BusyCellException();
        }
    }

    private void checkStartCellToMove(BoardCell cell)
            throws GeneralErrorException, WhiteCellException, InvalidMoveException {
        checkCellExists(cell);
        checkCellIsBlack(cell);
        checkTowerExists(cell);
        checkNoNeedToHitChecker(cell);

    }

    private void checkFinishCellToMove(BoardCell cell)
            throws BusyCellException, GeneralErrorException, WhiteCellException {
        checkCellExists(cell);
        checkCellIsBlack(cell);
        checkCellNotBusy(cell);

    }

    private void checkNoNeedToHitChecker(BoardCell cell) throws InvalidMoveException {
        Color color = cell.getTower().getColor();
        for (ArrayList<BoardCell> row : this.cells) {
            for (BoardCell checkerCell : row) {
                Tower tower = checkerCell.getTower();
                if (tower != null && tower.getColor() == color) {
                    checkTowersToHit(checkerCell);
                }
            }
        }
    }

    /**
     * Checks the ability to beat the checker at the beginning of the turn
     *
     * @param cell the cell on which the checker is placed at the beginning of the turn
     * @throws InvalidMoveException In the case when the checkers are moved without beating the checkers,
     *                              but there is a possibility of taking, an exception is called
     */
    private void checkTowersToHit(BoardCell cell) throws InvalidMoveException {
        if (isPossibleToHit(cell, 1, 1)) {
            throw new InvalidMoveException();
        }
        if (isPossibleToHit(cell, 1, -1)) {
            throw new InvalidMoveException();
        }
        if (isPossibleToHit(cell, -1, 1)) {
            throw new InvalidMoveException();
        }
        if (isPossibleToHit(cell, -1, -1)) {
            throw new InvalidMoveException();
        }

    }

    private void makeMove(BoardCell startCell, BoardCell finishCell) {
        Tower tower = startCell.getTower();
        tower.move(startCell, finishCell);

    }

    private void makeHit(BoardCell startCell, BoardCell finishCell)
            throws GeneralErrorException, BusyCellException, WhiteCellException {

        checkCellNotBusy(finishCell);
        checkCellIsBlack(finishCell);

        int startCellRow = startCell.getRowNumber();
        int startCellColumn = startCell.getColumnNumber();
        int finishCellRow = finishCell.getRowNumber();
        int finishCellColumn = finishCell.getColumnNumber();

        int offsetRow = (finishCellRow - startCellRow > 0) ? 1 : -1;
        int offsetColumn = (finishCellColumn - startCellColumn > 0) ? 1 : -1;

        int row = startCellRow + offsetRow;
        int column = startCellColumn + offsetColumn;

        Tower tower = startCell.getTower();
        Boolean isKing = tower.isKing();

        if (!isPossibleToHit(startCell, offsetRow, offsetColumn)) {
            throw new GeneralErrorException();
        }

        BoardCell targetCell = getCellByCoordinates(row, column);
        if (isKing) {
            while (targetCell.getTower() == null && coordinatesIsCorrect(row, column)) {
                row += offsetRow;
                column += offsetColumn;
                targetCell = getCellByCoordinates(row, column);
            }
        }

        tower.hit(startCell, finishCell, targetCell);

    }

    /**
     * Checks the possibility of taking checkers in the specified direction.
     *
     * @param cell         the cell of the board starting from which the check is performed
     * @param offsetRow    offset by table rows
     * @param offsetColumn offset by table columns
     * @return true if there is an opportunity to beat the checker else false
     */
    private boolean isPossibleToHit(BoardCell cell, int offsetRow, int offsetColumn) {
        boolean result = false;
        Boolean isKing = cell.getTower().isKing();
        int row = cell.getRowNumber() + offsetRow;
        int column = cell.getColumnNumber() + offsetColumn;

        while (coordinatesIsCorrect(row, column)) {
            BoardCell cellToHit = this.getCellByCoordinates(row, column);
            if (cellToHit == null) {
                break;
            }

            if (canHitTower(cell, cellToHit)) {
                result = true;
                break;
            }

            if (!isKing) {
                break;
            }
            row += offsetRow;
            column += offsetColumn;

        }

        return result;

    }

    /**
     * Checks the ability to beat a checker on the specified cell
     *
     * @param cell      original board cell
     * @param cellToHit specified cell
     * @return true if the opportunity to be a checker exists, else false
     */
    private boolean canHitTower(BoardCell cell, BoardCell cellToHit) {
        boolean result = false;
        Tower tower = cell.getTower();
        Tower towerToHit = cellToHit.getTower();
        if (tower != null && towerToHit != null && tower.getColor() != towerToHit.getColor()) {
            int startRow = cell.getRowNumber();
            int startColumn = cell.getColumnNumber();
            int hitRow = cellToHit.getRowNumber();
            int hitColumn = cellToHit.getColumnNumber();

            int finishRow = hitRow + ((hitRow > startRow) ? 1 : -1);
            int finishColumn = hitColumn + ((hitColumn > startColumn) ? 1 : -1);

            if (coordinatesIsCorrect(finishRow, finishColumn)) {
                BoardCell finishCell = getCellByCoordinates(finishRow, finishColumn);
                if (finishCell != null && finishCell.getTower() == null) {
                    result = true;
                }
            }

        }

        return result;

    }

}
