package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

/**
 * Singleton class representing the secret objective of the player in the client-side model.
 */
public class SecretObjective extends Observable {
    // Singleton instance
    private static SecretObjective INSTANCE;

    // Attribute holding the secret objective
    private ObjectiveRecord secretObjective;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private SecretObjective() {}

    /**
     * Retrieves the singleton instance of SecretObjective.
     *
     * @return The singleton instance of SecretObjective.
     */
    public synchronized static SecretObjective getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SecretObjective();
        }
        return INSTANCE;
    }

    /**
     * Retrieves the secret objective of the player.
     *
     * @return The secret objective of the player.
     */
    public ObjectiveRecord getSecretObjective() {
        return secretObjective;
    }

    /**
     * Sets the secret objective of the player.
     *
     * @param secretObjective The secret objective to be set.
     */
    public void setSecretObjective(ObjectiveRecord secretObjective) {
        this.secretObjective = secretObjective;
        super.updateObservers();
    }
}
