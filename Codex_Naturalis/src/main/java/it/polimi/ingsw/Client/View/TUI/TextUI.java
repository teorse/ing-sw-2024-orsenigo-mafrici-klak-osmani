package it.polimi.ingsw.Client.View.TUI;

import java.io.IOException;

/**
 * Text-based User Interface for interacting with the game.
 * <p>
 * The `TextUI` class is designed for command-line interactions with the user. It provides various methods to display
 * game information, get user input, and perform actions like clearing the terminal.
 * <p>
 * The primary functionalities of this class include:
 * - Displaying game titles, logos, and other introductory information.
 * - Interacting with the game through a text-based interface, allowing players to choose options and make game decisions.
 * - Providing detailed views of game components, such as card maps, card pools, and players' cards.
 * - Showing specific game-related information like shared objectives, points, and artifact counters.
 * - Utility functions for clearing the terminal and other miscellaneous operations.
 */
public class TextUI{
    //STATIC METHODS
    public static void displayGameTitle() {
        System.out.print("""
                 ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗
                ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝
                ██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗
                ██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║
                ╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║
                 ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝
                """);
    }

    public static void displayGameOver() {
        System.out.print("""
                 ██████╗  █████╗ ███╗   ███╗███████╗     ██████╗ ██╗   ██╗███████╗██████╗
                ██╔════╝ ██╔══██╗████╗ ████║██╔════╝    ██╔═══██╗██║   ██║██╔════╝██╔══██╗
                ██║  ███╗███████║██╔████╔██║█████╗      ██║   ██║██║   ██║█████╗  ██████╔╝
                ██║   ██║██╔══██║██║╚██╔╝██║██╔══╝      ██║   ██║╚██╗ ██╔╝██╔══╝  ██╔══██╗
                ╚██████╔╝██║  ██║██║ ╚═╝ ██║███████╗    ╚██████╔╝ ╚████╔╝ ███████╗██║  ██║
                 ╚═════╝ ╚═╝  ╚═╝╚═╝     ╚═╝╚══════╝     ╚═════╝   ╚═══╝  ╚══════╝╚═╝  ╚═╝
                """);
    }

    public static void displayLastRound() {
        System.out.print("""
                ██╗      █████╗ ███████╗████████╗    ██████╗  ██████╗ ██╗   ██╗███╗   ██╗██████╗
                ██║     ██╔══██╗██╔════╝╚══██╔══╝    ██╔══██╗██╔═══██╗██║   ██║████╗  ██║██╔══██╗
                ██║     ███████║███████╗   ██║       ██████╔╝██║   ██║██║   ██║██╔██╗ ██║██║  ██║
                ██║     ██╔══██║╚════██║   ██║       ██╔══██╗██║   ██║██║   ██║██║╚██╗██║██║  ██║
                ███████╗██║  ██║███████║   ██║       ██║  ██║╚██████╔╝╚██████╔╝██║ ╚████║██████╔╝
                ╚══════╝╚═╝  ╚═╝╚══════╝   ╚═╝       ╚═╝  ╚═╝ ╚═════╝  ╚═════╝ ╚═╝  ╚═══╝╚═════╝
                """);
    }

    public static void displayChatState() {
        System.out.print("""
                 ██████╗██╗  ██╗ █████╗ ████████╗
                ██╔════╝██║  ██║██╔══██╗╚══██╔══╝
                ██║     ███████║███████║   ██║
                ██║     ██╔══██║██╔══██║   ██║
                ╚██████╗██║  ██║██║  ██║   ██║
                 ╚═════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝
                """);
    }

    /**
     * Clears the terminal screen to remove any existing text.
     * <p>
     * The method first checks the operating system (OS) type. If the OS is Windows, it uses a Windows-specific
     * command to clear the terminal. For non-Windows systems (like macOS and Linux), it uses ANSI escape sequences
     * to clear the screen.
     * <p>
     * If an error occurs during the process, an exception stack trace is printed.
     */
    public static void clearCMD() {
        try {
            //Detects if the OS is Windows
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                //Command for Windows
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                //Command for macOS/Linux
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}