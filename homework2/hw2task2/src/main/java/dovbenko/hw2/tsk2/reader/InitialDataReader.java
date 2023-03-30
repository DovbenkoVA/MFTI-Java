package dovbenko.hw2.tsk2.reader;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Locale;

public final class InitialDataReader {

    private InitialDataReader() {
    }

    public static HashMap<String, String> readFromFile() throws IOException {
        String inputFileName = "input.txt";
        HashMap<String, String> result = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFileName))) {
            result.put("white", reader.readLine());
            result.put("black", reader.readLine());
            StringBuilder stringBuilder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line.toLowerCase(Locale.ROOT)).append(" ");
                line = reader.readLine();
            }
            result.put("moves", stringBuilder.toString());

        }
        return result;
    }

}
