package it.polimi.ingsw.Client.View;

public interface Observer {
    void update();

    //todo add cleanUp method to prepare observers for garbage collection
}
