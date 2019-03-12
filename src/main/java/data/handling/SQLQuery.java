package data.handling;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLQuery implements Callable<String> {
    private static final Logger LOGGER = Logger.getLogger(SQLQuery.class.getName());
    private final Connection connection;
    private final String preparedStatementQuery;
    private final PdfWriter pdfWriter;
    private final int batchSize;
    private final int batchNumber;
    private final String[] headers;
    private final int recordsPerPage;


    public SQLQuery(Connection connection, String preparedStatementQuery, PdfWriter pdfWriter, int batchNumber, int batchSize, int recordsPerPage, String... headers) throws SQLException {
        this.recordsPerPage = recordsPerPage;
        connection.setAutoCommit(false);
        this.connection = connection;
        this.preparedStatementQuery = preparedStatementQuery;
        this.pdfWriter=pdfWriter;
        this.batchSize = batchSize;
        this.batchNumber = batchNumber;
        this.headers = headers;
    }

    @Override
    public String call() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            preparedStatement = this.connection.prepareStatement(this.preparedStatementQuery);
            preparedStatement.setInt(1, batchSize);
            preparedStatement.setInt(2, (batchNumber) * batchSize);
            preparedStatement.setFetchSize(batchSize);
            resultSet = preparedStatement.executeQuery();
            //csv = csvWriter.format(resultSet, headers);
            /* Generate single pdf page */
            printRecords(resultSet);




        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failure", e);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }
            if (null != preparedStatement) {
                preparedStatement.close();
            }
        }
        return "" ;
    }

    private void printRecords(ResultSet resultSet) throws SQLException, IOException {
        int pageNumber = 0;
        List<List<String>> rows = new LinkedList<>(); //List of rows
        int rowCounter = 1;
        int columnCount = resultSet.getMetaData().getColumnCount();
        while(resultSet.next()) {
            List<String> columns = new LinkedList<>();  //List Of columns
            for(int i = 1; i <= columnCount; ++i) {
                Object obj = resultSet.getObject(i);
                if(obj == null) {
                 obj="";
                }
             CharSequence charSequence = obj instanceof CharSequence?(CharSequence)obj:obj.toString();
                columns.add(charSequence.toString());
            }
            rowCounter++;
            rows.add(columns);
            if(rowCounter == recordsPerPage){
               rowCounter =1;
               //FIXME : YYERUVA :  INVOKE pdf writer here.
                pdfWriter.print(rows, pageNumber);
                rows = new LinkedList<>(); //List of rows
                pageNumber++;
            }
        }

    }
}