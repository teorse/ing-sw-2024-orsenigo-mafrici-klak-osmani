package Server.Interfaces;

/**
 * The {@code LayerUser} interface serves as a marker interface
 * to identify classes representing users within different layers
 * of the server architecture.
 * <p>
 * Implementing classes are expected to provide a method to
 * retrieve the username associated with the user.
 * This interface does not mandate any specific behavior or
 * functionality beyond the getUsername() method.
 * <p>
 * By adhering to this interface, classes can be treated
 * uniformly across different layers of the server architecture,
 * facilitating code reuse and maintainability.
 */
public interface LayerUser {

    /**
     * Retrieves the username associated with the user.
     *
     * @return The username of the user.
     */
    String getUsername();
}
