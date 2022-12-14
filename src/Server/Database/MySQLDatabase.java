package Server.Database;

import java.sql.*;
import java.util.*;

/**
 * A SINGLETON class for MySQLDatabase connections and query execution.
 */
public class MySQLDatabase implements Database {
    private static final MySQLDatabase instance = new MySQLDatabase();

    private String connectionUrl;
    private String dbUser;
    private String dbPass;

    private MySQLDatabase() {
        if (instance != null) throw new InstantiationError("Creating this object is not allowed.");
    }

    /**
     * Returns the singleton instance of the class.
     * @return the singleton instance of the class.
     */
    public static MySQLDatabase getInstance() {
        return instance;
    }

    /* PRIVATE UTILITIES */

    /**
     * Opens a connection to the database.
     * @return the connection reference.
     */
    private Connection createConnection() {
        Connection result = null;
        Connection connection = null;

        // Get connection from driver manager
        if (connectionUrl != null) {
            try {
                connection = DriverManager.getConnection(connectionUrl, dbUser, dbPass);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            result = connection;
        }

        return result;
    }

    /**
     * Helps the creation of the prepared statement. Assigns the correct type to all references in queryParameters.
     * @param connection the database connection where the statement will be issued.
     * @param queryStr the query to be issued.
     * @param queryParameters the parameters of the query.
     * @return a ready-to-execute prepared statement.
     */
    private PreparedStatement prepareStatementHelper(Connection connection, String queryStr, List<Object> queryParameters) {
        PreparedStatement statement = null;

        try {
            // Initialize a prepared statement
            statement = connection.prepareStatement(queryStr);

            // Inserting all query parameters into the query ("?" placeholders will be replaced)
            if (queryParameters != null) {
                int placeholderIndex = 1;
                for (Object to_insert : queryParameters) {
                    if (to_insert instanceof Integer)
                        statement.setInt(placeholderIndex, (Integer) to_insert);
                    else if (to_insert instanceof String)
                        statement.setString(placeholderIndex, (String) to_insert);
                    placeholderIndex++;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (statement != null) {
            String queryPrint = statement.toString();
            System.out.println(queryPrint);
        }
        else
            System.err.println("Statement is null!!!");

        return statement;
    }

    /**
     * Converts a ResultSet as a List of Lists of Strings.
     * @param resultSet the result set to be converted
     * @return the result set as a list of lists of Strings
     */
    private ArrayList<ArrayList<String>> convertResultSet(ResultSet resultSet) {
        // Saving query results in a data structure (result set is lost after connection closed)
        ArrayList<ArrayList<String>> rowList = new ArrayList<>();

        try {
            ResultSetMetaData resultSetMeta = resultSet.getMetaData();

            // Saving query results in a data structure (result set is lost after connection closed)
            while (resultSet.next()) {
                ArrayList<String> colList = new ArrayList<>();

                for (int i = 1; i <= resultSetMeta.getColumnCount(); i++) {
                    Object to_add = resultSet.getString(i);
                    // ***[DEBUG]*** System.out.println("[DEBUG]: Extracted " + i + ": " + to_add.toString());
                    colList.add(to_add.toString());
                }
                rowList.add(colList);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        return rowList;
    }

    /**
     * {@inheritDoc}
     * @param url
     * @param user
     * @param pass
     */
    public void config(String url, String user, String pass) {
        System.out.println("Url: " + url);
        System.out.println("User: " + user);
        System.out.println("Pass: " + pass);

        this.connectionUrl = url;
        this.dbUser = user;
        this.dbPass = pass;
    }

    /* QUERY */

    /**
     * {@inheritDoc}
     * @param queryStr the query String to be executed with placeholders (?s) for parameters.
     * @param queryParameters a list of parameters.
     * @return a list of lists of results. Each row is a tuple. Each col is a tuple's col.
     * @throws SQLException if the query cannot be executed.
     */
    @Override
    public Object execQuery(String queryStr, List<Object> queryParameters) throws SQLException {
        // Declaring JDBC resources
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        // Query result (function output)
        ArrayList<ArrayList<String>> out;

        try {
            // Opening DB connection and initialize a prepared statement
            connection = createConnection();
            statement = prepareStatementHelper(connection, queryStr, queryParameters);

            System.out.println(">> Trying to execute: [" + statement.toString() + "]");

            // Executing the query and retrieving its metadata
            resultSet = statement.executeQuery();

            out = convertResultSet(resultSet);
        } catch (SQLException exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            // Releasing all JDBC resources
            try {
                if (connection != null) connection.close();
                if (statement != null) statement.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }

        return out;
    }

    /**
     * {@inheritDoc}
     * @param queryStr the query String to be executed.
     * @return a list of lists of results. Each row is a tuple. Each col is a tuple's col.
     * @throws SQLException if the query cannot be executed.
     */
    @Override
    public Object execQuery(String queryStr) throws SQLException {
        return execQuery(queryStr, null);
    }

    /* UPDATE (insert, delete, update) */

    /**
     * {@inheritDoc}
     * @param queryStr the query String to be executed with placeholders (?s) for parameters.
     * @param queryParameters a list of parameters
     * @return the number of rows updated
     * @throws SQLException if the query cannot be executed.
     */
    @Override
    public int execUpdate(String queryStr, List<Object> queryParameters) throws SQLException {
        // Declaring JDBC resources
        Connection connection = null;
        PreparedStatement statement = null;
        int updated_no;

        try {
            // Opening DB connection and initialize a prepared statement
            connection = createConnection();
            statement = prepareStatementHelper(connection, queryStr, queryParameters);

            // Executing the insert query
            updated_no = statement.executeUpdate();
        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            // Releasing all JDBC resources
            try {
                if (connection != null) connection.close();
                if (statement != null) statement.close();
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
        return updated_no;
    }

    /**
     * {@inheritDoc}
     * @param queryStr the query String to be executed.
     * @return the number of rows updated
     * @throws SQLException if the query cannot be executed.
     */
    @Override
    public int execUpdate(String queryStr) throws SQLException {
        return execUpdate(queryStr, null);
    }
}
