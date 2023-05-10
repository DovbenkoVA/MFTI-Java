package dovbenko.hw3.tsk2.logger;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public final class LoggerFormatter extends Formatter {
    @Override
    public String format(LogRecord record) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(record.getMillis());

        return sdf.format(calendar.getTime()) + "::"
                + record.getMessage() + "\n";
    }
}
