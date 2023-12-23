package edu.dental.service;

import edu.dental.domain.authentication.AuthenticationException;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.records.WorkRecordBook;
import edu.dental.domain.records.WorkRecordBookException;
import edu.dental.entities.User;

public final class AuthenticationService {

    private AuthenticationService() {}


    public static String registration(String name, String email, String password) throws AuthenticationException {
        User user = Authenticator.create(name, email, password);
        String jwt = Authenticator.JwtUtils.generateJwtFromEntity(user);
        Repository.getInstance().put(user, WorkRecordBook.createNew(user.getId()));
        return jwt;
    }

    public static String authorization(String login, String password) throws AuthenticationException {
        User user = Authenticator.authenticate(login, password);
        String jwt = Authenticator.JwtUtils.generateJwtFromEntity(user);
        try {
            Repository.getInstance().put(user, WorkRecordBook.getInstance(user));
        } catch (WorkRecordBookException e) {
            throw new AuthenticationException(AuthenticationException.Causes.ERROR);
        }
        return jwt;
    }

    public static int verification(String jwt) {
        return Authenticator.JwtUtils.getId(jwt);
    }
}
