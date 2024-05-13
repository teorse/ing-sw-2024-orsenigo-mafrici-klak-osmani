package Network.ClientServer;

/**
 * The ServerCommands enum represents the allowed values for commands related to server actions in ClientServerMessage header.
 * These commands are used to communicate server-side actions to the client.
 */
public enum ServerCommands {

    JOIN_LOBBY,
    START_LOBBY,
    LOG_OUT,
    LOG_IN,
    SIGN_UP,
    VIEW_LOBBY_PREVIEWS,
    STOP_VIEWING_LOBBY_PREVIEWS
}
