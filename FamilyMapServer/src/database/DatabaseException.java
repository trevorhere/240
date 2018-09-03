package database;

import java.sql.SQLException;

public class DatabaseException extends Throwable {
    public DatabaseException(String openConnection_failed, SQLException e) {
    }

    public DatabaseException(String s) {
    }
}
