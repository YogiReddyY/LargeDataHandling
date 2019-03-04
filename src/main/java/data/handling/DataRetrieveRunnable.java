package data.handling;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataRetrieveRunnable implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(DataRetrieveRunnable.class.getName());

    @Override
    public void run()  {
        PreparedStatement preparedStatement = null;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "admin")) {
            connection.setAutoCommit(false);
            LOGGER.info("Connected to PostgresSQL database!" + connection.getMetaData());
            preparedStatement = connection.prepareStatement("select id,first_name,middle_name,last_name,client_name,org_name,org_id,manager_name,lead_name,\n" +
                    "pin,city,country,LongLong\n" +
                    "from test.user limit 1000100");
            preparedStatement.setFetchSize(10000);
            retrieveData(preparedStatement);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Postgres Connection Failure.", e);
        } finally {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Closing Prepared Statement Failed :", e);
            }
        }
    }

    public static void retrieveData(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
            writeToCSV(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Execute Query Failure", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to Open or Create the file", e);
        } finally {
            resultSet.close();
        }
    }

    public static void writeToCSV(ResultSet resultSet) throws IOException, SQLException {
        final String SAMPLE_CSV_FILE = "C:\\Users\\yyeruva\\Desktop\\Assignment\\sample.csv";
        long startTime = System.nanoTime();
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("ID", "FirstName", "MiddleName", "LastName", "clientName", "OrgName", "OrgId", "ManagerName", "LeadName", "pin", "city", "country", "longLat"));

            csvPrinter.printRecords(resultSet);
            csvPrinter.flush();
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            LOGGER.info("Time Elapsed ::" + elapsedTime);
            LOGGER.info("CSV Writing  time in milliseconds : " +
                    elapsedTime / 1000000);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to Open or Create the file", e);
        } finally {
            writer.close();
        }
    }
}