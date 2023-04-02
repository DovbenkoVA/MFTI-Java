package dovbenko.hw2.tsk1;

import java.util.Collections;
import java.util.Locale;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Board {
    private static final Pattern POSITIONS_PATTERN = Pattern.compile("\\b[a-h][1-8]");
    private static final Pattern QUINS_POSITIONS_PATTERN = Pattern.compile("\\b[A-H][1-8]");
    private static final Pattern MOVEMENTS_PATTERN =
            Pattern.compile("([a-h][1-8])-([a-h][1-8])|([a-h][1-8])(:[a-h][1-8])+");
    private static final Pattern MOVE_PATTERN = Pattern.compile("([a-h][1-8])-([a-h][1-8])");
    private static final Pattern HIT_PATTERN = Pattern.compile("([a-h][1-8])(:[a-h][1-8])+");
    private static final char[] COORDINATE_SYMBOLS = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h'};
    private static final int SIZE = 8;

    private final HashMap<String, BoardCell> boardIndex;
    private String stringMovies;

    private final ArrayList<ArrayList<BoardCell>> cells;

    /**
     * Initializes the composition of the cells of the new checkers board
     *
     * @return table of the checkers
     */
    private ArrayList<ArrayList<BoardCell>> initializeNewCells() {
        ArrayList<ArrayList<BoardCell>> result = new ArrayList<>();
        Color color = Color.BLACK;
        for (int rowNumber = 0; rowNumber < SIZE; rowNumber++) {
            ArrayList<BoardCell> row = new ArrayList<>();
            for (int columnNumber = 0; columnNumber < SIZE; columnNumber++) {
                String cellName = nameByCoordinates(rowNumber, columnNumber);
                row.add(new BoardCell(this, cellName, rowNumber, columnNumber, color));
                color = getOppositeColor(color);
            }
            result.add(row);
            color = getOppositeColor(color);
        }
        return result;
    }

    public Board() {
        this.cells = initializeNewCells();
        this.boardIndex = newBoardIndex();

    }

    public static int getSize() {
        return SIZE;
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

    public static String nameByCoordinates(int row, int column) {

        return String.valueOf(Board.COORDINATE_SYMBOLS[column]) + (row + 1);

    }

    public BoardCell getCellByCoordinates(int row, int column) {
        BoardCell result = null;
        if (coordinatesIsCorrect(row, column)) {
            result = cells.get(row).get(column);
        }
        return result;
    }

    public BoardCell getCellByName(String cellName) {
        String searchKey = cellName.toLowerCase(Locale.ROOT);

        return boardIndex.get(searchKey);

    }

    private void moveChecker(BoardCell startCell, BoardCell finishCell) {
        Checker checker = startCell.getChecker();
        checker.move(finishCell);

    }

    private Color getOppositeColor(Color color) {
        return (color == Color.BLACK) ? Color.WHITE : Color.BLACK;
    }

    private void makeMove(String record)
            throws BusyCellException, GeneralErrorException, WhiteCellException, InvalidMoveException {
        Matcher matcher = POSITIONS_PATTERN.matcher(record);
        matcher.find();
        BoardCell startCell = getCellByName(matcher.group());
        checkStartCellToMove(startCell);

        matcher.find();
        BoardCell finishCell = getCellByName(matcher.group());

        checkFinishCellToMove(finishCell);
        moveChecker(startCell, finishCell);

    }

    private void makeHit(BoardCell startCell, BoardCell finishCell)
            throws BusyCellException, WhiteCellException, GeneralErrorException {

        Checker checker = startCell.getChecker();

        checkCellBusy(finishCell);
        checkCellColor(finishCell);
        Boolean isKing = checker.getIsKing();

        int startCellRow = startCell.getRowNumber();
        int startCellColumn = startCell.getColumnNumber();
        int finishCellRow = finishCell.getRowNumber();
        int finishCellColumn = finishCell.getColumnNumber();

        int deltaRow = (finishCellRow - startCellRow > 0) ? 1 : -1;
        int deltaColumn = (finishCellColumn - startCellColumn > 0) ? 1 : -1;

        int row = startCellRow + deltaRow;
        int column = startCellColumn + deltaColumn;

        if (!isPossibleToHit(startCell, deltaRow, deltaColumn)) {
            throw new GeneralErrorException();
        }

        BoardCell targetCell = getCellByCoordinates(row, column);
        if (isKing) {
            while (targetCell.getChecker() == null && coordinatesIsCorrect(row, column)) {
                row += deltaRow;
                column += deltaColumn;
                targetCell = getCellByCoordinates(row, column);
            }
        }

        Checker targetChecker = targetCell.getChecker();
        checker.hit(targetChecker, finishCell);

    }

    private void makeHits(String record)
            throws BusyCellException, GeneralErrorException, WhiteCellException {
        Matcher matcher = POSITIONS_PATTERN.matcher(record);
        matcher.find();
        BoardCell startCell = getCellByName(matcher.group());

        checkCheckerExists(startCell);

        while (matcher.find()) {
            BoardCell nextCell = getCellByName(matcher.group());
            makeHit(startCell, nextCell);
            startCell = nextCell;
        }
        Checker checker = startCell.getChecker();
        checker.checkSetIsKing();
    }

    private void checkFinishCellToMove(BoardCell cell)
            throws BusyCellException, GeneralErrorException, WhiteCellException {
        checkCellExists(cell);
        checkCellColor(cell);
        checkCellBusy(cell);

    }

    private void checkStartCellToMove(BoardCell cell)
            throws GeneralErrorException, WhiteCellException, InvalidMoveException {
        checkCellExists(cell);
        checkCellColor(cell);
        checkCheckerExists(cell);
        checkMustToHit(cell);

    }

    private void checkMustToHit(BoardCell cell) throws InvalidMoveException {
        Color color = cell.getChecker().getColor();
        for (ArrayList<BoardCell> row : this.cells) {
            for (BoardCell checkerCell : row) {
                Checker checker = checkerCell.getChecker();
                if (checker != null && checker.getColor() == color) {
                    checkCheckersToHit(checkerCell);
                }
            }
        }
    }

    private void checkCellColor(BoardCell cell) throws WhiteCellException {
        if (cell.getColor() == Color.WHITE) {
            throw new WhiteCellException();
        }
    }

    private void checkCellExists(BoardCell cell) throws GeneralErrorException {
        if (cell == null) {
            throw new GeneralErrorException();
        }
    }

    private void checkCellBusy(BoardCell cell) throws BusyCellException {
        if (cell.getChecker() != null) {
            throw new BusyCellException();
        }
    }

    private void checkCheckerExists(BoardCell cell) throws GeneralErrorException {
        if (cell.getChecker() == null) {
            throw new GeneralErrorException();
        }
    }

    /**
     * Checks the ability to beat the checker at the beginning of the turn
     * @param cell the cell on which the checker is placed at the beginning of the turn
     * @throws InvalidMoveException In the case when the checkers are moved without beating the checkers,
     * but there is a possibility of taking, an exception is called
     */
    private void checkCheckersToHit(BoardCell cell) throws InvalidMoveException {
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

    private boolean coordinatesIsCorrect(int row, int column) {
        return row >= 0 && row < Board.SIZE && column >= 0 && column < Board.SIZE;
    }

    /**
     * CChecks the ability to beat a checker on the specified cell
     * @param cell original board cell
     * @param cellToHit specified cell
     * @return
     */
    private boolean canHitChecker(BoardCell cell, BoardCell cellToHit) {
        Boolean result = false;
        Checker checker = cell.getChecker();
        Checker checkerToHit = cellToHit.getChecker();
        if (checker != null && checkerToHit != null && checker.getColor() != checkerToHit.getColor()) {
            int startRow = cell.getRowNumber();
            int startColumn = cell.getColumnNumber();
            int hitRow = cellToHit.getRowNumber();
            int hitColumn = cellToHit.getColumnNumber();

            int finishRow = hitRow + ((hitRow > startRow) ? 1 : -1);
            int finishColumn = hitColumn + ((hitColumn > startColumn) ? 1 : -1);

            if (coordinatesIsCorrect(finishRow, finishColumn)) {
                BoardCell finishCell = getCellByCoordinates(finishRow, finishColumn);
                if (finishCell != null && finishCell.getChecker() == null) {
                    result = true;
                }
            }

        }

        return result;

    }

    /**
     * Checks the possibility of taking checkers in the specified direction.
     * @param cell the cell of the board starting from which the check is performed
     * @param deltaRow offset by table rows
     * @param deltaColumn offset by table columns
     * @return true if there is an opportunity to beat the checker else false
     */
    private boolean isPossibleToHit(BoardCell cell, int deltaRow, int deltaColumn) {
        Boolean result = false;
        Boolean isKing = cell.getChecker().getIsKing();
        int boardSize = Board.getSize();
        int row = cell.getRowNumber();
        int column = cell.getColumnNumber();
        row += deltaRow;
        column += deltaColumn;
        while (row >= 0 && row < boardSize && column >= 0 && column < boardSize) {
            BoardCell cellToHit = this.getCellByCoordinates(row, column);
            if (cellToHit != null) {
                if (canHitChecker(cell, cellToHit)) {
                    result = true;
                    break;
                }

            } else {
                break;
            }

            if (!isKing) {
                break;
            }
            row += deltaRow;
            column += deltaColumn;
        }

        return result;

    }

    public void setStartingPosition(String stringPosition, Color color) {
        Matcher matcher = Board.POSITIONS_PATTERN.matcher(stringPosition);
        while (matcher.find()) {
            BoardCell boardCell = getCellByName(matcher.group(0));
            Checker checker = new Checker(boardCell, color, false);
            boardCell.setChecker(checker);
        }
        matcher = Board.QUINS_POSITIONS_PATTERN.matcher(stringPosition);
        while (matcher.find()) {
            BoardCell boardCell = getCellByName(matcher.group(0));
            Checker checker = new Checker(boardCell, color, true);
            boardCell.setChecker(checker);
        }
    }

    public void setStringMovies(String stringMovies) {
        this.stringMovies = stringMovies;
    }

    private Boolean isHit(String record) {
        return Board.HIT_PATTERN.matcher(record).find();
    }

    private Boolean isMove(String record) {
        return Board.MOVE_PATTERN.matcher(record).find();
    }

    public void makeMovementsAndHits()
            throws BusyCellException, GeneralErrorException, WhiteCellException, InvalidMoveException {
        Matcher matcher = Board.MOVEMENTS_PATTERN.matcher(this.stringMovies);
        while (matcher.find()) {
            String record = matcher.group();
            if (isMove(record)) {
                makeMove(matcher.group());
            } else if (isHit(record)) {
                makeHits(matcher.group());
            }
        }
    }

    private HashMap<Color, ArrayList<String>> getCheckersPositions() {
        HashMap<Color, ArrayList<String>> result = new HashMap<>();
        ArrayList<String> namesWhite = new ArrayList<String>();
        ArrayList<String> namesBlack = new ArrayList<String>();

        for (int j = 0; j < SIZE; j++) {
            for (int i = 0; i < SIZE; i++) {
                BoardCell cell = getCellByCoordinates(i, j);
                Checker checker = cell.getChecker();
                if (checker != null) {
                    String cellName = cell.getName();
                    if (checker.getIsKing()) {
                        cellName = cellName.toUpperCase(Locale.ROOT);
                    }
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
     * Returns the final position of the checkers on the board.
     * @return a string containing the final arrangement of checkers on the board.
     */
    public String stringCheckersPositions() {
        HashMap<Color, ArrayList<String>> checkersPositions = getCheckersPositions();
        StringBuilder stringBuilder = new StringBuilder();
        for (String name: checkersPositions.get(Color.WHITE)) {
            stringBuilder.append(name).append(" ");
        }
        stringBuilder.append("\n");
        for (String name: checkersPositions.get(Color.BLACK)) {
            stringBuilder.append(name).append(" ");
        }
        return stringBuilder.toString();

    }

}
