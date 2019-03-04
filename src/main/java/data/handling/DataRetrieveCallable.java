package data.handling;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yyeruva on 03-03-2019.
 */

public class DataRetrieveCallable implements Callable<String> {
    private static final Logger LOGGER = Logger.getLogger(DataRetrieveCallable.class.getName());
    String result;

    @Override
    public String call() throws Exception {
        PreparedStatement preparedStatement = null;
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "admin")) {
            connection.setAutoCommit(false);
            LOGGER.info("Connected to PostgresSQL database!" + connection.getMetaData());
            preparedStatement = connection.prepareStatement("select id,first_name,middle_name,last_name,client_name,org_name,org_id,manager_name,lead_name,\n" +
                    "pin,city,country,LongLong\n" +
                    "from test.user");
            result = retrieveData(preparedStatement);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failure.", e);
        } finally {
            preparedStatement.close();
        }
        return result;
    }

    public static String retrieveData(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = null;
        try {
            resultSet = preparedStatement.executeQuery();
            getListOfUsers(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failure.", e);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "CSV File Not accessible", e);
        } finally {
            resultSet.close();
        }
        return "successfully";
    }

    public static List<User> getListOfUsers(ResultSet resultSet) throws SQLException, IOException {
        List<User> userList = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(new BigInteger(String.valueOf(Integer.valueOf(resultSet.getInt(1)))).toString());
            user.setFirstName(resultSet.getString(2));
            user.setMiddleName(resultSet.getString(3));
            user.setLastName(resultSet.getString(4));
            user.setClientName(resultSet.getString(5));
            user.setOrgName(resultSet.getString(6));
            user.setOrgId(resultSet.getString(7));
            user.setManagerName(resultSet.getString(8));
            user.setLeadName(resultSet.getString(9));
            user.setPin(resultSet.getString(10));
            user.setCity(resultSet.getString(11));
            user.setCountry(resultSet.getString(12));
            user.setLongLat(resultSet.getString(13));
            userList.add(user);
        }
        writeToCSV(userList);
        LOGGER.info("UserListSize::" + userList.size());
        return userList;
    }

    public static void writeToCSV(List<User> userList) throws IOException {
        final String SAMPLE_CSV_FILE = "C:\\Users\\yyeruva\\Desktop\\Assignment\\sample.csv";
        long startTime = System.nanoTime();
        BufferedWriter writer = null;
        try {
            writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("ID", "FirstName", "MiddleName", "LastName", "clientName", "OrgName", "OrgId", "ManagerName", "LeadName", "pin", "city", "country", "longLat"));

            for (int i = 0; i < userList.size(); i++) {
                csvPrinter.printRecord(userList.get(i).getId(), userList.get(i).getFirstName(), userList.get(i).getLastName(), userList.get(i).getClientName(), userList.get(i).getOrgName(), userList.get(i).getOrgId(), userList.get(i).getManagerName(), userList.get(i).getLeadName(), userList.get(i).getPin(), userList.get(i).getCity(), userList.get(i).getCountry(), userList.get(i).getLongLat());
            }
            csvPrinter.flush();
            long endTime = System.nanoTime();
            long elapsedTime = endTime - startTime;
            LOGGER.info("Time Elapsed ::" + elapsedTime);
            LOGGER.info("CSV Writing  time in milliseconds : " +
                    elapsedTime / 1000000);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "IO Exception", e);
        } finally {
            writer.close();
        }
    }
}