package dental.database;

import java.sql.SQLException;
import java.sql.Statement;

public final class InitializerDB {
    private InitializerDB() {}

    public static final String DROPS = "DROP TABLE IF EXISTS mydb.";

    public static final String ACCOUNTS = """
            CREATE TABLE mydb.accounts (
                id INT NOT NULL,
                login VARCHAR(15) NOT NULL,
                PRIMARY KEY (id)
                );""";

    public static final String RECORDS = """
            CREATE TABLE mydb.records (
            	account_id INT NOT NULL,
            	FOREIGN KEY(account_id) REFERENCES mydb.accounts(id) ON DELETE CASCADE,
            	id INT NOT NULL,
            	patient VARCHAR(20),
            	clinic VARCHAR(20),
            	complete DATE,
            	accepted DATE NOT NULL,
            	closed BOOLEAN,
            	PRIMARY KEY (id)
                );""";

    public static final String WORKS_OBJECTS = """
            CREATE TABLE mydb.work_position (
            	record_id INT NOT NULL,
                title VARCHAR(15) NOT NULL,
                quantity SMALLINT,
                price INT NOT NULL,
                FOREIGN KEY(record_id) REFERENCES mydb.records(id) ON DELETE CASCADE
                );""";

    public static final String REPORTS = """
            CREATE TABLE mydb.reports (
            	r_year YEAR NOT NULL,
            	r_month ENUM('january', 'february', 'march',
            					'april', 'may', 'june', 'july',
                                'august', 'september', 'october',
                                'november', 'december') NOT NULL,
            	r_id INT NOT NULL AUTO_INCREMENT,
            	account_id INT NOT NULL,
                FOREIGN KEY(account_id) REFERENCES mydb.accounts(id)  ON DELETE CASCADE,
            	PRIMARY KEY (r_id, account_id)
            	);""";

    static {
        try {
            Statement statement = DBManager.getStatement();
            statement.addBatch(DROPS + "reports");
            statement.addBatch(DROPS + "work_position");
            statement.addBatch(DROPS + "records");
            statement.addBatch(DROPS + "accounts");
            statement.addBatch(ACCOUNTS);
            statement.addBatch(RECORDS);
            statement.addBatch(WORKS_OBJECTS);
            statement.addBatch(REPORTS);
            statement.executeBatch();
        } catch (SQLException e) {
            //TODO loggers
            throw new RuntimeException(e);
        }
    }
}
