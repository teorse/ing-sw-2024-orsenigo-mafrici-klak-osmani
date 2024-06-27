package it.polimi.ingsw.Client.View;

import it.polimi.ingsw.CommunicationProtocol.LanIpFinder;

public abstract class InputValidator {

    /**
     * Validates an IPv4 address.
     * This method checks if the provided string is a valid IPv4 address. A valid IPv4 address
     * consists of four octets, each ranging from 0 to 255, separated by periods (".").
     *
     * @param ip the string representation of the IPv4 address to validate
     * @return {@code true} if the string is a valid IPv4 address, {@code false} otherwise
     */
    public static boolean isValidIPAddress(String ip) {
        return LanIpFinder.isValidIPAddress(ip);
    }

    /**
     * Checks if the provided name is valid based on the following criteria:
     * <ul>
     *     <li>The name is not null.</li>
     *     <li>The name does not contain any spaces.</li>
     *     <li>The name only contains alphanumeric characters and underscores.</li>
     *     <li>The name has a length between 4 and 14 characters, inclusive.</li>
     * </ul>
     *
     * @param name the name to be validated
     * @return {@code true} if the name is valid, {@code false} otherwise
     */
    public static boolean isNameValid(String name) {
        return name != null && !name.contains(" ") && name.matches("^[a-zA-Z]+$") && name.length() >= 4 && name.length() <= 14;
    }

    /**
     * Checks if the provided password is valid based on the following criteria:
     * <ul>
     *     <li>The password is not null.</li>
     *     <li>The password does not contain any spaces.</li>
     *     <li>The password has a length between 5 and 20 characters, inclusive.</li>
     * </ul>
     *
     * @param password the password to be validated
     * @return {@code true} if the password is valid, {@code false} otherwise
     */
    public static boolean isPasswordValid(String password) {
        return password != null && !password.contains(" ") && password.length() >= 5 && password.length() <= 20;
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
            return false;
        }

        return choice >= lowerBound && choice <= upperBound;
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
        return (int) ch >= lowerBound && (int) ch <= upperBound;
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
    public static boolean validBinaryChoice(String input) {
        int choice;
        try {
            choice = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return false;
        }

        // Check if the input is either 1 or 2
        return choice == 1 || choice == 2;
    }
}