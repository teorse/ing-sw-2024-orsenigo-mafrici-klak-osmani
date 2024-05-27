package Client.View;

import Client.Model.ClientModel;
import CommunicationProtocol.ServerClient.DataTransferObjects.LobbyUserRecord;
import CommunicationProtocol.ServerClient.DataTransferObjects.PlayerRecord;
import Server.Model.Lobby.LobbyRoles;

public abstract class UserInterface {

    /**
     * Checks the validity of an IP address.
     * <p>
     * This method validates whether the provided IP address is a valid IPv4 address.
     * An IP address is considered valid if it meets the following criteria:
     * - It is not null or empty.
     * - It consists of four parts separated by periods ('.').
     * - Each part is a numeric value between 0 and 255, inclusive.
     * <p>
     * If the provided string does not meet any of these criteria, the method returns false.
     * Otherwise, it returns true, indicating that the IP address is valid.
     *
     * @param ip The IP address string to be validated.
     * @return True if the IP address is valid according to IPv4 standards, otherwise false.
     */
    public static boolean isValidIPAddress(String ip) {
        // Split the string into parts separated by "."
        String[] parts = ip.split("\\.");

        // The IP address should have exactly 4 parts
        if (parts.length != 4) {
            System.out.println("\nThe IP address should have exactly 4 parts.");
            return false;
        }

        for (String part : parts) {
            try {
                int num = Integer.parseInt(part); // Convert the part to an integer

                // Check that the number is between 0 and 255
                if (num < 0 || num > 255) {
                    System.out.println("\nEach part of the address should be between 0 and 255.");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("\nError in the format of IP address.");
                return false; // If the part is not a valid number
            }
        }
        return true; // If all parts are valid and between 0 and 255
    }

    /**
     * Checks the validity of a name.
     * <p>
     * This method validates whether the provided name meets the specified criteria:
     * - It cannot be null or empty.
     * - It cannot contain spaces.
     * - It can only contain letters, numbers, and underscores.
     * - It must be at least 4 characters long and not exceed 14 characters in length.
     * <p>
     * If the name does not meet any of these criteria, an appropriate error message
     * is displayed, and the method returns false. Otherwise, it returns true.
     *
     * @param name The name to be validated.
     * @return True if the name is valid according to the specified criteria, otherwise false.
     */
    public static boolean isNameValid(String name) {

        if (name == null || name.isEmpty()) {
            System.out.println("\nInvalid input: name can't be empty.");
            return false;
        }

        if (name.contains(" ")) {
            System.out.println("\nInvalid input: name can't contain spaces.");
            return false;
        }

        if (!name.matches("^[a-zA-Z0-9_]+$")) {
            System.out.println("\nInvalid input: name can contain only letters, numbers and underscore.");
            return false;
        }

        if (name.equalsIgnoreCase("back")) {
            System.out.println("\nInvalid input: name can't be 'back'.");
            return false;
        }

        if (name.equalsIgnoreCase("quit")) {
            System.out.println("\nInvalid input: name can't be 'quit'.");
            return false;
        }

        if (name.length() < 4) {
            System.out.println("\nInvalid input: name must be at least 4 characters long.");
            return false;
        }

        if (name.length() > 14) {
            System.out.println("\nInvalid input: name can't be longer than 15 characters.");
            return false;
        }

        return true;
    }


    /**
     * Checks the validity of a password.
     * <p>
     * This method validates whether the provided password meets the specified criteria:
     * - It cannot be null or empty.
     * - It cannot contain spaces.
     * - It must be at least 5 characters long.
     * - It cannot exceed 20 characters in length.
     * <p>
     * If the password does not meet any of these criteria, an appropriate error message
     * is displayed, and the method returns false. Otherwise, it returns true.
     *
     * @param password The password to be validated.
     * @return True if the password is valid according to the specified criteria, otherwise false.
     */
    public static boolean isPasswordValid(String password) {

        if (password == null || password.isEmpty()) {
            System.out.println("\nInvalid input: password can't be empty.");
            return false;
        }

        if (password.contains(" ")) {
            System.out.println("\nInvalid input: password can't contain spaces.");
            return false;
        }

        if (password.equalsIgnoreCase("back")) {
            System.out.println("\nInvalid input: password can't be 'back'.");
            return false;
        }

        if (password.equalsIgnoreCase("quit")) {
            System.out.println("\nInvalid input: password can't be 'quit'.");
            return false;
        }

        if (password.length() < 5) {
            System.out.println("\nInvalid input: password must be at least 5 characters long.");
            return false;
        }

        if (password.length() > 20) {
            System.out.println("\nInvalid input: password can't be longer than 20 characters.");
            return false;
        }

        return true;
    }

    /**
     * Checks if a given string can be parsed as an integer.
     *
     * @param input the string to check
     * @return true if the string can be parsed as an integer, false otherwise
     */
    public static boolean isParsableAsInt(String input) {
        if (input == null || input.isEmpty()) {
            return false;
        }
        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Checks user input against specified bounds.
     * <p>
     * This method reads an integer input from the user and checks if it falls within the specified
     * lower and upper bounds (inclusive). If the input is not within the bounds, it prompts the user
     * to enter a valid number within the specified range and continues to do so until a valid input is provided.
     *
     * @param lowerBound The lower bound of the acceptable range.
     * @param upperBound The upper bound of the acceptable range.
     * @return The integer input entered by the user within the specified bounds.
     */
    public static boolean checkInputBound(String input, int lowerBound, int upperBound){
        int choice;

        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid input format: please enter a number between " + lowerBound + " and " + upperBound);
            return false;
        }

        if(choice >= lowerBound && choice <= upperBound)
            return true;
        else {
            System.out.println("\nInvalid input: number must be between " + lowerBound + " and " + upperBound);
            return false;
        }
    }

    /**
     * Checks if a given character is within the specified bounds.
     *
     * @param ch the character to check
     * @param lowerBound the lower bound (inclusive)
     * @param upperBound the upper bound (inclusive)
     * @return {@code true} if the character is within the bounds; {@code false} otherwise
     */
    public static boolean isCharWithinBounds(char ch, int lowerBound, int upperBound) {

        boolean withinBounds = (int) ch >= lowerBound && (int) ch <= upperBound;
        char lowerBoundChar = (char) lowerBound;
        char upperBoundChar = (char) upperBound;

        if (!withinBounds) {
            System.out.println("\nError: The character '" + ch + "' is not within the bounds '" +
                    lowerBoundChar + "' and '" + upperBoundChar + "'.");
        }

        return withinBounds;
    }



    /**
     * Reads a binary choice (1 or 2) from the user input.
     * <p>
     * This method prompts the user to enter a binary choice, either 1 or 2,
     * and reads the input from the console. It continues to prompt the user
     * until a valid input is provided.
     *
     * @return The binary choice entered by the user (1 or 2).
     */
    public static boolean getBinaryChoice(String input) {
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("\nInvalid format input. Please enter 1 or 2!");
            return false;
        }

        // Check if the input is either 1 or 2
        if (choice == 1 || choice == 2) {
            return true;
        } else {
            System.out.println("\nInvalid input. Please enter 1 or 2!");
            return false;
        }
    }

    /**
     * Retrieves the PlayerRecord corresponding to the current user from the ClientModel.
     *
     * <p>This method iterates over the player records stored in the ClientModel and compares each player's username with
     * the username of the current user. When a match is found, it returns the corresponding PlayerRecord.
     *
     * @param model The ClientModel containing player records and user information.
     * @return The PlayerRecord corresponding to the current user, or null if not found.
     */
    public static PlayerRecord usernameToPlayerRecord(ClientModel model, String username) {
        PlayerRecord PR = null;
        for (PlayerRecord playerRecord : model.getPlayers()) {
            if(playerRecord.username().equals(username)) {
                PR = playerRecord;
            }
        }
        return PR;
    }

    /**
     * Retrieves the LobbyUserRecord corresponding to the current user from the ClientModel.
     *
     * <p>This method iterates over the lobby user records stored in the ClientModel and compares each user's username with
     * the username of the current user. When a match is found, it returns the corresponding LobbyUserRecord.
     *
     * @param model The ClientModel containing lobby user records and user information.
     * @return The LobbyUserRecord corresponding to the current user, or null if not found.
     */
    public static LobbyUserRecord usernameToLobbyUserRecord(ClientModel model, String username) {
        LobbyUserRecord LUR = null;
        for (LobbyUserRecord lobbyUserRecord : model.getLobbyUserRecords()) {
            if (lobbyUserRecord.username().equals(username))
                LUR = lobbyUserRecord;
        }
        return LUR;
    }

    /**
     * Checks if the current user is an administrator in the lobby.
     *
     * <p>This method iterates over the lobby user records stored in the ClientModel and compares each user's username with
     * the username of the current user. If a match is found and the user's role is ADMIN, it returns true indicating that
     * the current user is an administrator. Otherwise, it returns false.
     *
     * @param model The ClientModel containing lobby user records and user information.
     * @return {@code true} if the current user is an administrator, {@code false} otherwise.
     */
    public static boolean areYouAdmin(ClientModel model) {
        for (LobbyUserRecord user : model.getLobbyUserRecords()) {
            if (user.username().equals(model.getMyUsername()) && user.role() == LobbyRoles.ADMIN)
                return true;
        }
        return false;
    }
}