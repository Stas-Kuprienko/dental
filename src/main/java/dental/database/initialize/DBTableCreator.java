package dental.database.initialize;

import dental.database.service.ConnectionPool;
import dental.database.service.DBConfig;
import dental.domain.userset.Account;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public final class DBTableCreator {

    private DBTableCreator() {}
    static {
        instance = new DBTableCreator();
    }
    private static final DBTableCreator instance;


    private static final String PRODUCT_MAP = """
            CREATE TABLE %s (
                id INT NOT NULL,
                title VARCHAR(15) NOT NULL,
                price INT NOT NULL,
                PRIMARY KEY (`id`)
            );""";

    private static final String WORK_RECORD = """
            CREATE TABLE %s (
                id INT NOT NULL AUTO_INCREMENT,
            	patient VARCHAR(20),
                clinic VARCHAR(20),
                %s
                complete DATE,
            	accepted DATE NOT NULL,
                closed BOOLEAN NOT NULL DEFAULT 0,
                paid BOOLEAN NOT NULL DEFAULT 0,
                photo BLOB,
                comment VARCHAR(45),
                PRIMARY KEY (id)
            );""";


    public boolean createProductMap(Account account) {
        String tableName = DBConfig.DATA_BASE + ".product_map_" + account.getId();
        String query = String.format(PRODUCT_MAP, tableName);
        return request(query);
    }

    public boolean createWorkRecords(Account account) {
        String[] productTypes = account.productMap.getAllTitles();
        if ((productTypes == null)||(productTypes.length == 0)) {
            throw new NullPointerException();
        }
        String productColumns = concatProductColumns(productTypes);
        String tableName = DBConfig.DATA_BASE + ".work_record_" + account.getId();
        String query = String.format(WORK_RECORD, tableName, productColumns);
        return request(query);
    }

    private String concatProductColumns(String[] types) {
        StringBuilder result = new StringBuilder();
        for (String s : types) {
            result.append(String.format("%s INT DEFAULT 0,\t\n", s));
        }
        return result.toString();
    }

    private boolean request(String query) {
        boolean isSuccess = false;
        Connection connection = null;
        Statement statement = null;
        try {
            connection = ConnectionPool.get();
            statement = connection.createStatement();
            isSuccess = statement.execute(query);
        } catch (SQLException e) {
            //TODO
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (SQLException e) {
                    //TODO
                }
            }
            if (connection != null) {
                ConnectionPool.put(connection);
            }
        }
        return isSuccess;
    }

    public static synchronized DBTableCreator getInstance() {
        return instance;
    }
}
