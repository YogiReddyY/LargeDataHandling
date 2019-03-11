package data.handling;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class PDFReportMain {
    private static final Logger LOGGER = Logger.getLogger(PDFReportMain.class.getName());

    private static final int recordsPerPdf = 50000;

    public static void main(String[] args) {
        long startTime = System.nanoTime();
        ExecutorService executor = Executors.newFixedThreadPool(1);
        WriteToPdf convertCallable = new WriteToPdf();
        executor.submit(convertCallable);
        long endTime = System.nanoTime();
        long elapsedTimeExec = endTime - startTime;
        LOGGER.info("Processed time in milliseconds : " +
                elapsedTimeExec / 1000000);
        executor.shutdown();
    }

    public void mergePdfDocuments() {
        long start = System.currentTimeMillis();
        try {
            PDFMergerUtility pdfMerger = new PDFMergerUtility();
            pdfMerger.setDestinationFileName("merged_doc.pdf");
            pdfMerger.addSource(new FileInputStream("users.pdf"));
            pdfMerger.addSource(new FileInputStream("users1.pdf"));
            pdfMerger.addSource(new FileInputStream("users2.pdf"));
            pdfMerger.addSource(new FileInputStream("users3.pdf"));
            pdfMerger.addSource(new FileInputStream("users4.pdf"));
            pdfMerger.addSource(new FileInputStream("users5.pdf"));
            pdfMerger.addSource(new FileInputStream("users6.pdf"));
            pdfMerger.addSource(new FileInputStream("users7.pdf"));
            pdfMerger.addSource(new FileInputStream("users8.pdf"));
            pdfMerger.addSource(new FileInputStream("users9.pdf"));
            pdfMerger.addSource(new FileInputStream("users10.pdf"));
            pdfMerger.addSource(new FileInputStream("users10K.pdf"));
            pdfMerger.addSource(new FileInputStream("users11.pdf"));
            pdfMerger.addSource(new FileInputStream("users12.pdf"));
            pdfMerger.addSource(new FileInputStream("users13.pdf"));
            pdfMerger.addSource(new FileInputStream("users14.pdf"));
            pdfMerger.addSource(new FileInputStream("users15.pdf"));
            pdfMerger.addSource(new FileInputStream("users16.pdf"));
            pdfMerger.addSource(new FileInputStream("users17.pdf"));
            pdfMerger.addSource(new FileInputStream("users18.pdf"));
            pdfMerger.addSource(new FileInputStream("users19.pdf"));
            pdfMerger.addSource(new FileInputStream("users20.pdf"));
            pdfMerger.addSource(new FileInputStream("users21.pdf"));
            pdfMerger.addSource(new FileInputStream("users22.pdf"));
            pdfMerger.addSource(new FileInputStream("users23.pdf"));
            long end = System.currentTimeMillis();
            LOGGER.info("TimeTaken To Merge PDF Files In Secs ::" + (end - start) / 1000);
            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Exception while trying to merge pdf document ", e);
        }
    }
   /*Used To Arrive no.of threads to be initiated and how many no.of pdfs to be created from the retrieved data */
    public static int chunkingData(int records){
        return records/recordsPerPdf;
    }
}



