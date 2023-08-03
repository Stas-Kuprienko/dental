package dental.app;

import dental.app.records.Record;
import dental.app.records.RecordManager;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Objects;

public class Account implements Serializable {

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The login for the user access.
     */
    private String login;

    /**
     * {@link MessageDigest MD5 hash} of the user password.
     */
    private byte[] password;

    /**
     * The {@link LocalDate date} the account was created. Auto-generates.
     */
    private final LocalDate created;

    /**
     * The RecordManager object for manipulating {@link Record records}.
     */
    public final RecordManager recordManager;

    /**
     * The HashMap of the HashMap of the reports,
     *  the string year is used as the Key.
     * Value is the HashMap of the monthly reports,
     *  in which the string month is used as the Key,
     *   and value is the Report object.
     */
    private HashMap<String, HashMap<String, TableReport>> reports;

    /**
     * Create a new Account object.
     * @param name     The user name.
     * @param login    Login for the user authorization.
     * @param password The password for access to the Account
     */
    public Account(String name, String login, String password) {
        this.created = LocalDate.now();
        this.recordManager = new RecordManager();
        if ((name == null || name.isEmpty())
          || (login == null || login.isEmpty())
          || (password == null || password.isEmpty())) {
            return;
        }
        this.name = name;
        this.login = login;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            this.password = md.digest(password.getBytes());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            System.exit(1);
        }
        this.reports = new HashMap<>();
    }

    /**
     * Verification the user's password when logging in.
     * @param password The user's password.
     * @return The {@link Account} object if verification was successful, or null if not.
     */
    public Account verifyPassword(byte[] password) {
        if (MessageDigest.isEqual(this.password, password)) {
            return this;
        }else {
            return null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(name, account.name)
                && Objects.equals(login, account.login)
                && Objects.equals(created, account.created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(created);
    }

    @Override
    public String toString() {
        return "Account{" + "name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", created=" + created +
                '}';
    }


    /*                           ||
            Getters and setters  \/
     */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public LocalDate getCreated() {
        return created;
    }

    public HashMap<String, HashMap<String, TableReport>> getReports() {
        return reports;
    }

}
