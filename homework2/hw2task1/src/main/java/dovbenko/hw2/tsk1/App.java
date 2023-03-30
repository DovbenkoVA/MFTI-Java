package dovbenko.hw2.tsk1;

import dovbenko.hw2.tsk1.exception.BusyCellException;
import dovbenko.hw2.tsk1.exception.GeneralErrorException;
import dovbenko.hw2.tsk1.exception.InvalidMoveException;
import dovbenko.hw2.tsk1.exception.WhiteCellException;
import dovbenko.hw2.tsk1.game.Board;
import dovbenko.hw2.tsk1.game.Color;
import dovbenko.hw2.tsk1.reader.InitialDataReader;

import java.io.IOException;
import java.util.HashMap;

public final class App {
    private App() {
    }

    public static String getFinalPosition(String startPositionWhite, String startPositionBlack, String movies) {
        String result = "";
        Board board = new Board();
        board.setStartingPosition(startPositionWhite, Color.WHITE);
        board.setStartingPosition(startPositionBlack, Color.BLACK);
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
