package Server.Queries.QueryClasses;

import java.util.ArrayList;
import java.util.List;

public class GetUserDataQuery extends MySQLQueryCommand {
    private final String username;

    public GetUserDataQuery(String username) {
        this.username = username;
    }

    @Override
    public Object execute() {
        String query = queriesHandler.getQuery("get_user_data.sql");
        List<Object> queryParameters = new ArrayList<>();

        queryParameters.add(username);

        return db.execQuery(query, queryParameters);
    }
}