package Server.Queries.QueryCommand;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GetUIDQuery extends MySQLQueryCommand{
    private final String username;

    public GetUIDQuery(String username) {
        this.username = username;
    }

    /**
     * Executes the query.
     * @return the result of the query.
     * @throws SQLException if the query cannot be executed.
     */
    @Override
    public Object execute() throws SQLException {
        String query = queriesHandler.getQuery("get_uid.sql");
        List<Object> queryParameters = new ArrayList<>();

        queryParameters.add(username);

        return db.execQuery(query, queryParameters);
    }
}
