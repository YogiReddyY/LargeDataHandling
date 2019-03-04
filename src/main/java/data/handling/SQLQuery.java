package data.handling;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLQuery implements Callable<String> {
    private static final Logger LOGGER = Logger.getLogger(SQLQuery.class.getName());
    private final Connection connection;
    private final String preparedStatementQuery;
    private final CSVWriter csvWriter;
    private final int pageSize;
    private final int pageNumber;
    private final String[] headers;

    public SQLQuery(Connection connection, String preparedStatementQuery, CSVWriter csvWriter, int pageNumber, int pageSize, String... headers) throws SQLException {
        connection.setAutoCommit(false);
        this.connection = connection;
        this.preparedStatementQuery = preparedStatementQuery;
        this.csvWriter = csvWriter;
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
        this.headers = headers;
    }

    @Override
    public String call() throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        StringWriter csv = null;
        try {
            preparedStatement = this.connection.prepareStatement(this.preparedStatementQuery);
            preparedStatement.setInt(1, pageSize);
            preparedStatement.setInt(2, (pageNumber) * pageSize);
            preparedStatement.setFetchSize(pageSize);
            resultSet = preparedStatement.executeQuery();
            csv = csvWriter.format(resultSet, headers);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Connection failure", e);
        } finally {
            if (null != resultSet) {
                resultSet.close();
            }
            if (null != preparedStatement) {
                preparedStatement.close();
            }
        }
        return csv.toString();
    }
}