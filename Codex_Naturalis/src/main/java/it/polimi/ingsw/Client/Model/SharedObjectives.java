package it.polimi.ingsw.Client.Model;

import it.polimi.ingsw.CommunicationProtocol.ServerClient.DataTransferObjects.ObjectiveRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton class representing the shared objectives in the client-side model.
 */
public class SharedObjectives extends Observable {
    // Singleton instance
    private static SharedObjectives INSTANCE;

    // Attribute holding the shared objectives
    private List<ObjectiveRecord> sharedObjectives;

    /**
     * Private constructor to enforce singleton pattern.
     */
    private SharedObjectives() {
        sharedObjectives = new ArrayList<>();
    }

    /**
     * Retrieves the singleton instance of SharedObjectives.
     *
     * @return The singleton instance of SharedObjectives.
     */
    public synchronized static SharedObjectives getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new SharedObjectives();
        }
        return INSTANCE;
    }

    /**
     * Retrieves a copy of the list of shared objectives.
     *
     * @return A new list containing the shared objectives.
     */
    public List<ObjectiveRecord> getSharedObjectives() {
        return new ArrayList<>(sharedObjectives);
    }

    /**
     * Sets the shared objectives to the specified list.
     *
     * @param sharedObjectives The list of shared objectives to be set.
     */
    public void setSharedObjectives(List<ObjectiveRecord> sharedObjectives) {
        if (sharedObjectives != null) {
            this.sharedObjectives = new ArrayList<>(sharedObjectives);
        } else {
            this.sharedObjectives = new ArrayList<>();
        }
        super.updateObservers();
    }
}
