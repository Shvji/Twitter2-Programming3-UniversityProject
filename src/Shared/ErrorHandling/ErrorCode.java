package Shared.ErrorHandling;

/**
 * An enum that provides error codes for the whole application.
 */
public enum ErrorCode {
    NONE,
    GENERIC_ERROR,
    INVALID_REQUEST,
    DB_NOT_FOUND,
    DB_SQL_ERROR,
    SESSION_ERROR,
    REGISTER_USER_EXISTS,
    LOGIN_USER_NOT_FOUND,
    LOGIN_WRONG_PASSWORD,
    NO_PARAMETERS,
    USER_NOT_FOUND,
    ALREADY_FOLLOWING,
    SUBMIT_TWEET_ERROR,
    FETCH_TWEETS_ERROR,
    ADMIN_NOT_FOUND,
    ADMIN_WRONG_PASSWORD,
    LOGOUT_ERROR,
    BANNED
}
