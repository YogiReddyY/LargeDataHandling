package data.handling;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

/**
 * Created by yyeruva on 03-03-2019.
 */
public class DataConnectivity {

    private static final Logger LOGGER = Logger.getLogger(DataConnectivity.class.getName());

    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {
        long startTime = System.nanoTime();
        ExecutorService executorService = Executors.newFixedThreadPool(800);
        DataRetrieveCallable dataRetrieveCallable = new DataRetrieveCallable();
        Future<String> future = executorService.submit(dataRetrieveCallable);
        String res = future.get();
        long execEndTime = System.nanoTime();
        LOGGER.info("Process Completed" + res);
        long elapsedTimeExec = execEndTime - startTime;
        LOGGER.info("Processed time in milliseconds : " +
                elapsedTimeExec / 1000000);
        executorService.shutdown();
    }
}
