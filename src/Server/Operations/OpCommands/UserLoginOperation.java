package Server.Operations.OpCommands;

import Server.Queries.QueryAdapter.QueryOneRowAdapter;
import Server.Queries.QueryAdapter.QueryUpdateAdapter;
import Server.Queries.QueryCommand.GetUserDataQuery;
import Server.Queries.QueryCommand.InsertUserSessionQuery;
import Server.Queries.QueryCommand.MySQLQueryCommand;
import Server.Utils.TwitterServerUtils;
import Shared.ErrorHandling.ErrorCode;
import Shared.ErrorHandling.Exceptions.BanException;
import Shared.ErrorHandling.Exceptions.SessionException;
import Shared.Packet.Packet;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class UserLoginOperation extends OperationCommand {
    private final Packet packet;

    /**
     * Sets both the socket and the packet needed in order to execute the operation.
     * @param socket the socket where packets will be sent or read.
     * @param packet the packet needed for the operation.
     */
    public UserLoginOperation(Socket socket, Packet packet) {
        super(socket);
        this.packet = packet;
    }

    /**
     * Executes the current command.
     * @return a response Packet.
     * @throws SessionException if the session provided by the client is invalid.
     * @throws SQLException if a query cannot be executed.
     * @throws BanException if a user tries to log in while banned.
     */
    @Override
    public Packet execute() throws SQLException, SessionException, BanException {
        Packet response = packet.clone();

        // data 0 = username, data 1 = password
        String username = (String) packet.data.get(0);
        String password = (String) packet.data.get(1);

        MySQLQueryCommand userData = new GetUserDataQuery(username);
        QueryOneRowAdapter userDataAdpt = new QueryOneRowAdapter(userData);

        // If no query result, user does not exist and login can't be performed
        List<?> result = userDataAdpt.execute();
        if (result.isEmpty()) {
            response.isSuccessful = false;
            response.errorCode = ErrorCode.LOGIN_USER_NOT_FOUND;
        }
        else {
            //is banned?
            if (result.get(5).equals("t")) {
                response.isSuccessful = false;
                response.errorCode = ErrorCode.BANNED;
                throw new BanException("The user is banned!");
            }

            String passwordHash = DigestUtils.sha256Hex(password);
            // get 2 = password, IF PASSWORDS ARE EQUALS THEN LOGIN!
            if (result.get(2).equals(passwordHash)) {
                // Generating session key
                String sessionKey = TwitterServerUtils.sessionKeyGenerator();

                // Inserting session into db
                // get 4 = uid
                MySQLQueryCommand insertSession = new InsertUserSessionQuery((String) result.get(4), sessionKey);
                QueryUpdateAdapter sessionQuery = new QueryUpdateAdapter(insertSession);

                // If session can be generated, success, else error
                if (sessionQuery.execute() != 0) {
                    response.isSuccessful = true;
                    // get 4 = uid
                    response.session.uid = (String) result.get(4);
                    response.session.username = username;
                    response.session.session_key = sessionKey;
                }
                else {
                    response.isSuccessful = false;
                    response.errorCode = ErrorCode.SESSION_ERROR;
                    throw new SessionException("Error on session insert!");
                }
            }
            else {
                response.isSuccessful = false;
                response.errorCode = ErrorCode.LOGIN_WRONG_PASSWORD;
            }
        }

        return response;
    }
}
