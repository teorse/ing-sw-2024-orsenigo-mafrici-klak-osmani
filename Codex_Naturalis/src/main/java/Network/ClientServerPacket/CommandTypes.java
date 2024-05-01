package Network.ClientServerPacket;

/**
 * The CommandTypes enum represents the allowed values for the first position of the header in ClientServerMessage.
 * It defines different execution layers such as SERVER, LOBBY, and GAME.
 */
public enum CommandTypes {
    SERVER,
    LOBBY,
    GAME
}
