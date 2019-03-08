package data.handling;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class PDFReportMain {
    private static final Logger LOGGER = Logger.getLogger(PDFReportMain.class.getName());

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        WriteToPdf convertCallable = new WriteToPdf();
        executor.submit(convertCallable);
        long endTime = System.nanoTime();
        long elapsedTimeExec = endTime - startTime;
        LOGGER.info("Processed time in milliseconds : " +
                elapsedTimeExec / 1000000);
        executor.shutdown();
    }
}
