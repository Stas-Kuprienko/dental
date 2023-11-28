package edu.dental.domain.authentication;

import edu.dental.database.DatabaseService;
import edu.dental.database.DatabaseException;
import edu.dental.domain.APIManager;
import edu.dental.domain.entities.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Authenticate a user if such exists.
 */
public final class Authenticator {

    private Authenticator() {}


    /**
     * The user authentication in system.
     * @param login    The user's account login.
     * @param password The password to verify.
     * @return The {@link User} object, if authentication was successful.
     * @throws AuthenticationException Causes of throwing
     *  - specified user is not found;
     *  - Database exception;
     *  - incorrect password;
     *  - a given argument is null.
     */
    public static User authenticate(String login, String password) throws AuthenticationException {
        if ((login == null || login.isEmpty())||(password == null || password.isEmpty())) {
            throw new AuthenticationException(AuthenticationException.Causes.NULL);
        }
        User user;
        DatabaseService databaseService = APIManager.INSTANCE.getDatabaseService();
        try {
            user = databaseService.authenticate(login, password);
        } catch (DatabaseException e) {
            throw new AuthenticationException(e);
        }
        if (user == null) {
            throw new AuthenticationException(AuthenticationException.Causes.NO);
        }
        if (!(verification(user, password))) {
            throw new AuthenticationException(AuthenticationException.Causes.PASS);
        }
        return user;
    }

    /**
     * Verification the user's password when logging in.
     * @param password The user's password.
     * @return The {@link User} object if verification was successful, or null if not.
     */
    public static boolean verification(User user, String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] password2 = md.digest(password.getBytes());
            return MessageDigest.isEqual(user.getPassword(), password2);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verification(User user, byte[] password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return MessageDigest.isEqual(user.getPassword(), password);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Get the {@link MessageDigest MD5} hash of the password.
     * @param password The user's password string.
     * @return byte array of the password hash.
     */
    public static byte[] passwordHash(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            //TODO logger
            throw new RuntimeException(e);
        }
    }
}