package Server.Model;

import Exceptions.Server.LogInExceptions.IncorrectPasswordException;

/**
 * This class stores the hashed password and salt information associated with a user's login,
 * and provides a method to validate password attempts.
 */
public class ServerUserInfo {
    //ATTRIBUTES
    private final ServerUser serverUser;
    private final String hashedPassword;
    private final String salt;





    //CONSTRUCTOR

    /**
     * Creates a new ServerUserInfo object for the provided user.
     * @param serverUser Server user for whom this info is being created.
     * @param hashedPassword Hash of the password that will be stored for this user.
     * @param salt String that was used to salt the hashing process of the password.
     */
    public ServerUserInfo(ServerUser serverUser, String hashedPassword, String salt) {
        this.serverUser = serverUser;
        this.hashedPassword = hashedPassword;
        this.salt = salt;
    }





    //GETTERS
    /**
     * Retrieves the ServerUser associated with this ServerUserInfo object
     * if the provided password attempt matches the stored hashed password.
     *
     * @param passwordAttempt The password attempt to validate.
     * @return The ServerUser associated with this ServerUserInfo object.
     * @throws IncorrectPasswordException If the provided password attempt is incorrect.
     */
    public ServerUser getUser(String passwordAttempt) throws IncorrectPasswordException {
        String hashedAttempt = ServerSecurity.hashPassword(passwordAttempt, salt);
        if(hashedPassword.equals(hashedAttempt))
            return serverUser;
        else
            throw new IncorrectPasswordException(serverUser.getUsername(), "");
    }
}
