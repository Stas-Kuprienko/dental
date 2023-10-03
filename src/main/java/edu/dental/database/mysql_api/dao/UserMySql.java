package edu.dental.database.mysql_api.dao;

import edu.dental.database.DatabaseException;
import edu.dental.database.connection.DBConfiguration;
import edu.dental.database.interfaces.DAO;
import edu.dental.domain.authentication.Authenticator;
import edu.dental.domain.entities.User;
import edu.dental.utils.data_structures.MyList;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.*;
import java.util.Collection;

public class UserMySql implements DAO<User> {

    public static final String TABLE = DBConfiguration.DATA_BASE + ".user";

    public static final String FIELDS = "id, name, login, password, created";

    @Override
    public boolean put(User object) throws DatabaseException {
        String injections = "?, ".repeat(FIELDS.split(", ").length - 1);
        injections = "DEFAULT, " + injections.substring(0, injections.length() - 2);
        String query = String.format(MySqlSamples.INSERT.QUERY, TABLE, FIELDS, injections);
        try (Request request = new Request(query)) {
            byte i = 2;
            PreparedStatement statement = request.getStatement();
            statement.setString(i++, object.getName());
            statement.setString(i++, object.getLogin());
            statement.setBlob(i++, new SerialBlob(object.getPassword()));
            statement.setDate(i, Date.valueOf(object.getCreated()));
            statement.executeUpdate();
            return request.setID(object);
        } catch (SQLException e) {
            //TODO logger
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public Collection<User> getAll() throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_ALL.QUERY, "*", TABLE);
        MyList<User> usersList;
        try (Request request = new Request(query)) {
            ResultSet resultSet = request.getStatement().executeQuery();
            usersList = (MyList<User>) new UserInstantiation(resultSet).build();
        } catch (SQLException | IOException e) {
            //TODO logger
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
        return usersList;
    }

    @Override
    public User get(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, "id = ?");
        ResultSet resultSet;
        try (Request request = new Request(query)) {
            request.getStatement().setInt(1, id);
            resultSet = request.getStatement().executeQuery();
            MyList<User> list = (MyList<User>) new UserInstantiation(resultSet).build();
            return list.get(0);
        } catch (SQLException | NullPointerException | IOException e) {
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public User search(Object value1, Object value2) throws DatabaseException {
        String where = "login = ? AND password = ?";
        byte[] password = Authenticator.passwordHash((String) value2);
        String query = String.format(MySqlSamples.SELECT_WHERE.QUERY, "*", TABLE, where);
        ResultSet resultSet;
        try (Request request = new Request(query)) {
            request.getStatement().setString(1, (String) value1);
            request.getStatement().setBlob(2, new SerialBlob(password));
            resultSet = request.getStatement().executeQuery();
            MyList<User> result = (MyList<User>) new UserInstantiation(resultSet).build();
            return result.get(0);
        } catch (SQLException | IOException | NullPointerException | ClassCastException e) {
            //TODO logger
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    @Override
    public boolean edit(User object, Object... args) throws DatabaseException {
        return false;
    }

    @Override
    public boolean delete(int id) throws DatabaseException {
        String query = String.format(MySqlSamples.DELETE.QUERY, TABLE, "id = ?");
        try (Request request = new Request(query)) {
            request.getStatement().setInt(1, id);
            return request.getStatement().execute();
        } catch (SQLException e) {
            //TODO logger
            throw new DatabaseException(e.getMessage(), e.getCause());
        }
    }

    protected static class UserInstantiation implements Instantiating<User> {

        private final MyList<User> usersList;
        private final ResultSet resultSet;

        protected UserInstantiation(ResultSet resultSet) {
            this.usersList = new MyList<>();
            this.resultSet = resultSet;
        }

        @Override
        public Collection<User> build() throws SQLException, IOException {
            try (resultSet) {
                while (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setLogin(resultSet.getString("login"));
                    Blob blob = resultSet.getBlob("password");
                    byte[] password = blob.getBinaryStream().readAllBytes();
                    user.setPassword(password);
                    user.setCreated(resultSet.getDate("created").toLocalDate());
                    usersList.add(user);
                }
            }
            return this.usersList;
        }
    }

}
