package test;

import database.Database;
import database.DatabaseException;
import org.junit.Test;
import services.EventService;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static junit.framework.TestCase.assertEquals;

public class EventServiceTest {
    Database db = new Database();

    @Test
    public void getUserFromToken()
    {

        registerUser();
        EventService eventService = new EventService();
        assertEquals(eventService.getUserFromToken("Test"),true);

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
    public void getEventByID()
    {
        assertEquals(3,3);
    }
}