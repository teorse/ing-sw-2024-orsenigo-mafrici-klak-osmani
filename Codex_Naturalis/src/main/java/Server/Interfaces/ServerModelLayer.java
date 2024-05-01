package Server.Interfaces;

/**
 * The ServerModelLayer interface defines the contract for each layer of the server model.
 * It guarantees that all layers have methods to handle both accidental and voluntary user disconnections from the server.
 */
public interface ServerModelLayer {

    /**
     * Performs the necessary procedures when a user is accidentally disconnected from the layer.
     *
     * @param user The identifier of the user who got disconnected.
     */
    void userDisconnectionProcedure(LayerUser user);

    /**
     * Performs the necessary procedures when a user voluntarily logs out from the layer.
     *
     * @param user The identifier of the user who logged out.
     */
    void logOut(LayerUser user);
}
