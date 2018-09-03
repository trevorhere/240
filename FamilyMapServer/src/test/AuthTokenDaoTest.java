package test;

import database.Database;
import database.DatabaseException;
import model.AuthToken;
import org.junit.Test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static junit.framework.TestCase.assertEquals;

public class AuthTokenDaoTest {


    Database db = new Database();

    @Test
    public void storeAuthToken() {

        db.start();
        ResultSet resultSet = null;
        Statement stmt = null;

        AuthToken testToken = new AuthToken();
        AuthToken token = new AuthToken();

        testToken.username = "test";
        testToken.authToken = "test";
        testToken.personID = "test";

        try {

            db.authTokenDao.storeAuthToken(testToken);
            stmt = db.conn.createStatement();
            resultSet = stmt.executeQuery("select * from authTokens where authTokens.authToken = '" + "test" + "'");

            while (resultSet.next()) {
                token.setUsername(resultSet.getString(1));
                token.setAuthToken(resultSet.getString(2));
                token.setPersonID(resultSet.getString(3));
            }

            assertEquals(testToken.username, token.username);
            assertEquals(testToken.authToken, token.authToken);
            assertEquals(testToken.personID, token.personID);

            Statement delete = null;
            delete = db.conn.createStatement();
            delete.executeUpdate("delete from authTokens where authTokens.authToken = '" + "test" + "'");


            db.closeConnection(true);


            if (stmt != null) {
                stmt.close();
            }
            if (resultSet != null) {
                resultSet.close();
            }
            if (delete != null) {
                delete.close();
            }
        } catch (SQLException e) {
            e.getErrorCode();
        } catch (DatabaseException e) {
            e.getMessage();
        }

    }
}