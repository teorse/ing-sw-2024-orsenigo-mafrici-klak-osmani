package it.polimi.ingsw.Client.View;

/**
 * The Observer interface represents an object that observes changes in Observable objects.
 * Observers implementing this interface can be notified when there are updates in the observed objects.
 */
public interface Observer {
    /**
     * This method is called by an Observable object when it notifies its observers of a change.
     * Observers should implement this method to perform actions when they are notified of updates.
     */
    public void update();
}
