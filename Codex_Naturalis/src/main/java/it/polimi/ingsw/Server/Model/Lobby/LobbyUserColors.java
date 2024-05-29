package it.polimi.ingsw.Server.Model.Lobby;

/**
 * Enum representing the colors that a user can be.
 */
public enum LobbyUserColors {
    RED("\033[0;31m"),
    BLUE("\033[0;34m"),
    GREEN("\033[0;32m"),
    YELLOW("\033[0;33m");

    // String representation for terminal display
    private final String displayString;

    /**
     * Constructor for the LobbyUserColors enum.
     *
     * @param terminalColor the string representation of the color for terminal display
     */
    LobbyUserColors(String terminalColor) {
        this.displayString = terminalColor;
    }

    /**
     * Gets the string representation of the color for terminal display.
     *
     * @return the string representation of the color
     */
    public String getDisplayString() {
        return displayString;
    }
}
