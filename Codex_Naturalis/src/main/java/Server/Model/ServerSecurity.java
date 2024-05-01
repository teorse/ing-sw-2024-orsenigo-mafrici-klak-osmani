package Server.Model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * This abstract class contains static methods used for server security procedures.<br>
 * Currently, it provides functionality for password hashing.
 */
public abstract class ServerSecurity {
    /**
     * Hashes a password using SHA-256 algorithm with a provided salt.
     *
     * @param password The password to be hashed.
     * @param salt     The salt used for hashing.
     * @return A string representing the hashed password.
     */
    public static String hashPassword(String password, String salt){
        try{
            //Choosing hashing algorithm for hashing passwords
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            //Adding salt to the message digest
            md.update(Base64.getDecoder().decode(salt));
            //Hashing the password with salted message digest
            byte[] hashedPassword = md.digest(password.getBytes());
            //Return hashed password re-encoded to a string
            return Base64.getEncoder().encodeToString(hashedPassword);
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

/**
 * Generates a random salt for password hashing.
 *
 * @return A string representing the generated salt.
 */
    public static String generateSalt(){
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16];
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
}
