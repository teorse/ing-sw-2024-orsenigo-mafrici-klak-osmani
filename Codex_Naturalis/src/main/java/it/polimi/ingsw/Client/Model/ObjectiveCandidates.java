package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.List;

/**
 * Singleton class representing the list of objective candidates available in the client-side model.
 */
public class ObjectiveCandidates extends Observable {
    // Singleton instance
    private static ObjectiveCandidates INSTANCE;

    // Attributes
    private List<ObjectiveRecord> objectiveCandidates;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private ObjectiveCandidates() {}

    /**
     * Retrieves the singleton instance of the ObjectiveCandidates class.
     *
     * @return The singleton instance of ObjectiveCandidates.
     */
    public synchronized static ObjectiveCandidates getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ObjectiveCandidates();
        }
        return INSTANCE;
    }

    /**
     * Retrieves the list of objective candidates.
     *
     * @return The list of objective candidates.
     */
    public List<ObjectiveRecord> getObjectiveCandidates() {
        return objectiveCandidates;
    }

    /**
     * Retrieves a specific objective candidate by index.
     *
     * @param index The index of the objective candidate to retrieve.
     * @return The objective candidate at the specified index.
     */
    public ObjectiveRecord getObjectiveCandidatesByIndex(int index) {
        return objectiveCandidates.get(index);
    }

    /**
     * Sets the list of objective candidates.
     *
     * @param objectiveCandidates The list of objective candidates to set.
     */
    public void setCandidates(List<ObjectiveRecord> objectiveCandidates) {
        this.objectiveCandidates = objectiveCandidates;
    }
}
