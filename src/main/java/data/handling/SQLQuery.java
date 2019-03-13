package data.handling;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.*;
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
        this.pdfWriter = pdfWriter;
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
            //convertToStreamSource(resultSet);
            printRecords(resultSet);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failure", e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }
            if (null != preparedStatement) {
                preparedStatement.close();
            }
        }
        return "";
    }

    private void printRecords(ResultSet resultSet) throws SQLException, IOException {
        int pageNumber = 0;
        List<List<String>> rows = new LinkedList<>(); //List of rows
        int rowCounter = 0;
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
               rowCounter =0;
                pdfWriter.print(rows, pageNumber);
                rows = new LinkedList<>(); //List of rows
                pageNumber++;
            }
        }
        if(rowCounter < recordsPerPage){
            pdfWriter.print(rows, pageNumber);
        }

    }

    private void convertToStreamSource(ResultSet resultSet) throws Exception {
        int pageNumber = 0;
        LOGGER.info("From Convert XML");
        ResultSetMetaData rsmd = resultSet.getMetaData();
        int colCount = rsmd.getColumnCount();
        DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        org.w3c.dom.Document doc = builder.newDocument();
        org.w3c.dom.Element results = doc.createElement("Results");
        doc.appendChild(results);
        while (resultSet.next()) {
            org.w3c.dom.Element row = doc.createElement("Row");
            results.appendChild(row);
            for (int ii = 1; ii <= colCount; ii++) {
                String columnName = rsmd.getColumnName(ii);
                Object value = resultSet.getObject(ii);
                org.w3c.dom.Element node = doc.createElement(columnName);
                node.appendChild(doc.createTextNode(value.toString()));
                row.appendChild(node);
            }
            pageNumber++;
        }

        Source source = new DOMSource(doc);
        File file = new File("test.xml");
        Result result = new StreamResult(file);
        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(source, result);
        pdfWriter.print(new StreamSource(file), pageNumber);
    }

}