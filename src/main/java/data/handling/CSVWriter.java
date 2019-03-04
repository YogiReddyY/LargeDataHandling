package data.handling;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CSVWriter {
    private static final Logger LOGGER = Logger.getLogger(CSVWriter.class.getName());

    public StringWriter format(ResultSet resultSet, String[] headers) {
        long startTime = System.nanoTime();
        StringWriter stringWriter = new StringWriter();
        try {
            CSVPrinter csvPrinter = new CSVPrinter(stringWriter, CSVFormat.DEFAULT.withHeader(headers));
            csvPrinter.printRecords(resultSet);
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            LOGGER.info("\t\t\t\t\t\t\tCSV Writing  time in milliseconds : " +
                    elapsedTime / 1000000);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Exception", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQLException", e);
        }
        stringWriter.flush();
        return stringWriter;
    }
}
