package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.GameRecord;

/**
 * Singleton class representing the current state of the game.
 * This class manages attributes related to the ongoing game session,
 * such as completed rounds, setup status, and flags for the last round
 * and reconnection states.
 */
public class Game extends Observable {

    // Singleton instance
    private static Game INSTANCE;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private Game() {}

    /**
     * Retrieves the singleton instance of the Game class.
     *
     * @return The singleton instance of Game.
     */
    public synchronized static Game getInstance(){
        if(INSTANCE == null){
            INSTANCE = new Game();
        }
        return INSTANCE;
    }

    // Game attributes
    private int roundsCompleted;
    private boolean lastRoundFlag;
    private boolean setupFinished;
    private boolean waitingForReconnections;

    /**
     * Updates the game state based on the provided {@link GameRecord}.
     * Updates attributes like rounds completed, last round flag, setup status,
     * and waiting for reconnections flag. Notifies observers of the state change.
     *
     * @param game The {@link GameRecord} containing updated game state information.
     */
    public void updateGame(GameRecord game){
        roundsCompleted = game.roundsCompleted();
        lastRoundFlag = game.lastRoundFlag();
        setupFinished = game.setupFinished();
        waitingForReconnections = game.waitingForReconnections();

        super.updateObservers();
    }

    /**
     * Retrieves the number of rounds completed in the current game session.
     *
     * @return The number of rounds completed.
     */
    public int getRoundsCompleted() {
        return roundsCompleted;
    }

    /**
     * Checks if the game is in its last round.
     *
     * @return {@code true} if it is the last round, {@code false} otherwise.
     */
    public boolean isLastRoundFlag() {
        return lastRoundFlag;
    }

    /**
     * Checks if the game setup phase has finished.
     *
     * @return {@code true} if the setup phase is finished, {@code false} otherwise.
     */
    public boolean isSetupFinished() {
        return setupFinished;
    }

    /**
     * Checks if the game is currently waiting for player reconnections.
     *
     * @return {@code true} if waiting for reconnections, {@code false} otherwise.
     */
    public boolean isWaitingForReconnections() {
        return waitingForReconnections;
    }
}
