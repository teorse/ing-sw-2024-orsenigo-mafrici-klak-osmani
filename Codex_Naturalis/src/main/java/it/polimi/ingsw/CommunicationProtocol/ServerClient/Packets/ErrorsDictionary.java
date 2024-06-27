package it.polimi.ingsw.CommunicationProtocol.ServerClient.Packets;

/**
 * Enum representing various error messages that can be sent from the server to the client.
 */
public enum ErrorsDictionary {
    /**
     * Error indicating that the user is already logged in.
     */
    YOU_ARE_ALREADY_LOGGED_IN,

    /**
     * Error indicating that the provided password is incorrect.
     */
    WRONG_PASSWORD,

    /**
     * Error indicating that the username was not found.
     */
    USERNAME_NOT_FOUND,

    /**
     * Error indicating that the username is already taken.
     */
    USERNAME_ALREADY_TAKEN,

    /**
     * Error indicating that the account is already logged in by someone else.
     */
    ACCOUNT_ALREADY_LOGGED_IN_BY_SOMEONE_ELSE,

    /**
     * Error indicating that the lobby name is already taken.
     */
    LOBBY_NAME_ALREADY_TAKEN,

    /**
     * Error indicating that the lobby size is invalid.
     */
    INVALID_LOBBY_SIZE,

    /**
     * Error indicating that the lobby name was not found.
     */
    LOBBY_NAME_NOT_FOUND,

    /**
     * Error indicating that the lobby is closed.
     */
    LOBBY_IS_CLOSED,

    /**
     * Error indicating a generic error.
     */
    GENERIC_ERROR,
}
