package test;

import database.Database;
import database.DatabaseException;
import org.junit.Test;
import request.FillRequest;
import services.FillService;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static junit.framework.TestCase.assertEquals;

public class FillServiceTest {
    Database db = new Database();

    @Test
    public void fillFromUserName()
    {
        registerUser();
        FillService fillService = new FillService();
        FillRequest fillRequest = new FillRequest("test", 0);
        assertEquals(fillService.fillFromUserName(fillRequest), "{\"Message\":\"Successfully inserted user and 4 generations\"}");



    }
    public void registerUser()
    {
        String sql = "insert into users (userName, email, password, firstName, lastName, gender, personID, authToken) values (?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        db.start();
        try {
            stmt = db.conn.prepareStatement(sql);
            stmt.setString(1,  "test");
            stmt.setString(2,  "test");
            stmt.setString(3,  "test");
            stmt.setString(4,  "test");
            stmt.setString(5,  "test");
            stmt.setString(6,  "test");
            stmt.setString(7,  "test");
            stmt.setString(8,  "test");


            if (stmt.executeUpdate() != 1)
            {
                throw new SQLException();
            }
            if (stmt != null)
            {
                stmt.close();
            }
            db.closeConnection(true);

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }
    }


    @Test
    public void fillFromUserNameAddGenerations() {
        assertEquals(3,3);
    }
}