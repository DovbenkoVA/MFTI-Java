package dovbenko.hw2.tsk2;

import dovbenko.hw2.tsk2.exception.BusyCellException;
import dovbenko.hw2.tsk2.exception.GeneralErrorException;
import dovbenko.hw2.tsk2.exception.InvalidMoveException;
import dovbenko.hw2.tsk2.exception.WhiteCellException;
import dovbenko.hw2.tsk2.game.Board;
import dovbenko.hw2.tsk2.reader.InitialDataReader;

import java.io.IOException;
import java.util.HashMap;

public final class App {
    private App() {
    }

    public static String getFinalPosition(String startPositionWhite, String startPositionBlack, String movies) {
        String result;
        Board board = new Board();
        board.setStartingPosition(startPositionWhite);
        board.setStartingPosition(startPositionBlack);
        board.setStringMovies(movies);
        try {
            board.makeMovementsAndHits();
            result = board.stringCheckersPositions();
        } catch (BusyCellException | WhiteCellException | GeneralErrorException | InvalidMoveException e) {
            result = e.getMessage();

        }
        return result;
    }

    public static void main(String[] args) {
        try {
            HashMap<String, String> positionsAndMoves = InitialDataReader.readFromFile();
            String startPositionWhite = positionsAndMoves.get("white");
            String startPositionBlack = positionsAndMoves.get("black");
            String movies = positionsAndMoves.get("moves");
            System.out.println(getFinalPosition(startPositionWhite, startPositionBlack, movies));

        } catch (IOException e) {
            System.out.println("general error");

        }

    }
}
