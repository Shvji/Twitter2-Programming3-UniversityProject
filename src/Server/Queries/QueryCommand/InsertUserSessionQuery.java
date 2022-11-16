package Server.Queries.QueryCommand;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InsertUserSessionQuery extends MySQLQueryCommand {
    private final String uid;
    private final String sessionKey;

    /**
     * Sets the data used by the query.
     * @param uid the user logging in
     * @param sessionKey the session that will be recorded
     */
    public InsertUserSessionQuery(String uid, String sessionKey) {
        this.uid = uid;
        this.sessionKey = sessionKey;
    }

    /**
     * Executes the query.
     * @return the result of the query.
     * @throws SQLException if the query cannot be executed.
     */
    @Override
    public Object execute() throws SQLException {
        String query = queriesHandler.getQuery("insert_user_session.sql");
        List<Object> queryParameters = new ArrayList<>();

        queryParameters.add(uid);
        queryParameters.add(sessionKey);

        return db.execUpdate(query, queryParameters);
    }
}
