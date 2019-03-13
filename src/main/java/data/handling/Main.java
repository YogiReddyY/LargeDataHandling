package data.handling;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
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
    private static final int PAGE_SIZE = 100;
    private static int threadCount = 1;
    private static int pageSize;
    private static final int REC_PER_PAGE = 30;
    private static int pageCount;


    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {
        long startTime = System.nanoTime();
        String preparedStatement = "select id,first_name,middle_name,last_name,client_name,org_name," +
                "org_id,manager_name,lead_name,pin,city,country,LongLong " +
                "from test.user " +
                "LIMIT ? OFFSET ?;";
        List<Future<String>> taskList = new ArrayList<>();
        //CSVWriter csvWriter = new CSVWriter();
        //FIXME  : YYERUVA GIVE ME CORRECT PREFIX AND FOLDER PATH
        PdfWriter pdfWriter = new PdfWriter("Data","C:\\tmp");
        final Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/test", "postgres", "admin");
        int recordsCount = getRecordsCount(connection);
        LOGGER.info("Records Count"+ recordsCount);
        //pageCount = (recordsCount/REC_PER_PAGE)+(recordsCount%REC_PER_PAGE);
        //threadCount = (recordsCount/pageCount);
        LOGGER.info("Computed Thread Count"+ threadCount);
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);

        submitTask(executor, preparedStatement, taskList, pdfWriter, connection);
        //waitAndWriteCSVInOrder(taskList);
        // greedyWriteCSV(taskList);
        //FIXME : yyeruva : call merge all the resulted pdf files
        printEndTiming(startTime);
        executor.shutdown();
        //connection.close();
    }

    private static void waitAndWriteCSVInOrder(List<Future<String>> taskList) throws ExecutionException, InterruptedException {
        awaitCompletionAll(taskList);
        writeCSVFile(taskList);
    }

    private static void greedyWriteCSV(List<Future<String>> taskList) throws ExecutionException, InterruptedException {
        File file = new File("C:\\\\Users\\\\yyeruva\\\\Desktop\\\\Assignment\\\\sample.csv");
        try (FileWriter fileWriter = new FileWriter(file)) {
            while (!taskList.isEmpty()) {
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

    private static void submitTask(ExecutorService executor, String preparedStatement, List<Future<String>> taskList, PdfWriter pdfWriter, Connection connection) throws SQLException {
        for (int batchNumber = 0; batchNumber < threadCount; batchNumber++) {
            SQLQuery sqlQuery = new SQLQuery(connection, preparedStatement, pdfWriter,batchNumber, PAGE_SIZE, REC_PER_PAGE,  "ID",
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
                LOGGER.info("Sleeping for 100MS");
                Thread.sleep(100);
            } catch (InterruptedException e) {
                LOGGER.log(Level.SEVERE, "Current Thread Was Interrupted", e);
            }
        }
    }

    private static int getRecordsCount(Connection con) throws SQLException {
        ResultSet rs = con.prepareStatement("select count(*) from test.user").executeQuery();
        if (rs.next()) {
            return rs.getInt(1);
        } else {
            System.out.println("error: could not get the record counts");
        }
        return 0;
    }
}
