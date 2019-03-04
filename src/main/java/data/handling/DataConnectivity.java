package data.handling;

import java.sql.SQLException;
import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Created by yyeruva on 03-03-2019.
 */
public class DataConnectivity {

    private static final Logger LOGGER = Logger.getLogger(DataConnectivity.class.getName());

    public static void main(String[] args) throws SQLException, ExecutionException, InterruptedException {
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(800);
        DataRetrieveRunnable dataRetrieveCallable = new DataRetrieveRunnable();
        executor.execute(dataRetrieveCallable);
        long execEndTime = System.nanoTime();
        long elapsedTimeExec = execEndTime - startTime;
        LOGGER.info("Processed time in milliseconds : " +
                elapsedTimeExec / 1000000);
        executor.shutdown();
    }
}
