package data.handling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final int PAGE_SIZE = 250025;
    private static final int THREAD_COUNT = 4;

    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        String preparedStatement = "select id,first_name,middle_name,last_name,client_name,org_name," +
                "org_id,manager_name,lead_name,pin,city,country,LongLong " +
                "from test.user " +
                "LIMIT ? OFFSET ?;";
        List<Future<String>> taskList = new ArrayList<>();
        CSVWriter csvWriter = new CSVWriter();

        final Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "admin");


        submitTask(executor, preparedStatement, taskList, csvWriter, connection);
        //waitAndWriteCSVInOrder(taskList);
       greedyWriteCSV(taskList);
        printEndTiming(startTime);
        executor.shutdown();
        connection.close();
    }

    private static void waitAndWriteCSVInOrder(List<Future<String>> taskList) throws ExecutionException, InterruptedException {
        awaitCompletionAll(taskList);
        writeCSVFile(taskList);
    }

    private static void greedyWriteCSV(List<Future<String>> taskList) throws ExecutionException, InterruptedException {
        File file = new File("C:\\\\Users\\\\yyeruva\\\\Desktop\\\\Assignment\\\\sample.csv");
        try (FileWriter fileWriter = new FileWriter(file)) {
            while(!taskList.isEmpty()) {
                for (int task = 0; task < taskList.size(); task++) {
                    final Future<String> completedTask = taskList.get(task);
                    if (completedTask.isDone()) {
                        fileWriter.write(completedTask.get());
                        taskList.remove(task);
                    }
                }
                try {
                    LOGGER.info("Sleeping for 3 seconds");
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.SEVERE, "Current Thread Was Interrupted", e);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to Open or Create the file", e);

        }
    }
    private static void writeCSVFile(List<Future<String>> taskList) throws InterruptedException, ExecutionException {
        final int taskSize = taskList.size();
        File file = new File("C:\\\\Users\\\\yyeruva\\\\Desktop\\\\Assignment\\\\sample.csv");
        try (FileWriter fileWriter = new FileWriter(file)) {
            for (int task = 0; task < taskSize; task++) {
                final Future<String> completedTask = taskList.get(task);
                fileWriter.write(completedTask.get());
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to Open or Create the file", e);

        }
    }

    private static void printEndTiming(long startTime) {
        long execEndTime = System.nanoTime();
        long elapsedTimeExec = execEndTime - startTime;
        LOGGER.info(String.format("Processed time in milliseconds : %d", elapsedTimeExec / 1000000));
    }

    private static void submitTask(ExecutorService executor, String preparedStatement, List<Future<String>> taskList, CSVWriter csvWriter, Connection connection) throws SQLException {
        for (int page = 0; page < THREAD_COUNT; page++) {
            SQLQuery sqlQuery = new SQLQuery(connection, preparedStatement, csvWriter, page, PAGE_SIZE, "ID",
                    "FirstName",
                    "MiddleName",
                    "LastName",
                    "clientName",
                    "OrgName",
                    "OrgId",
                    "ManagerName",
                    "LeadName",
                    "pin",
                    "city",
                    "country",
                    "longLat");
            final Future<String> task = executor.submit(sqlQuery);
            taskList.add(task);
        }
    }

    private static void awaitCompletionAll(List<Future<String>> taskList) {
        int threadCounter = 0;
        int taskSize = taskList.size();
        while (true) {
            threadCounter = 0;
            for (int task = 0; task < taskSize; task++) {
                if (taskList.get(task).isDone()) {
                    threadCounter++;
                }
            }
            if (threadCounter == taskSize) {
                break;
            }
            try {
                LOGGER.info("Sleeping for 3 seconds");
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Current Thread Was Interrupted", e);
            }
        }
    }
}
