package test;

import database.Database;
import database.DatabaseException;
import model.Event;
import model.User;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertEquals;

public class EventDaoTest {
    Database db = new Database();


    @Test
    public void getUserFromToken()
    {

        ResultSet resultSet = null;
        Statement stmt = null;
        User funcUser = new User();

         registerUser();

         try
         {
             db.start();
             funcUser = db.eventDao.getUserFromToken("test");
             db.closeConnection(true);
         }
         catch (DatabaseException e)
         {
             e.getMessage();
         }


        assertEquals(funcUser.username, "test");
        assertEquals(funcUser.email, "test");
        assertEquals(funcUser.password, "test");
        assertEquals(funcUser.firstName, "test");
        assertEquals(funcUser.lastName, "test");
        assertEquals(funcUser.gender, "test");
        assertEquals(funcUser.personID, "test");
        assertEquals(funcUser.authToken, "test");

        deleteTestUser();

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
    public void deleteTestUser()
    {
        Statement stmt = null;
        try {
            db.start();
             stmt = db.conn.createStatement();
             stmt.executeUpdate("delete from users where users.authToken =  '" + "test" + "'");
             db.closeConnection(true);

             if(stmt != null)
             {
                 stmt.close();
             }

            }
        catch (SQLException e)
        {
            e.getErrorCode();
        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }
    }
    public void deleteTestEvent()
    {
        Statement stmt = null;
        try {
            db.start();
            stmt = db.conn.createStatement();
            stmt.executeUpdate("delete from events where events.eventID =  '" + "test" + "'");
            db.closeConnection(true);

            if(stmt != null)
            {
                stmt.close();
            }

        }
        catch (SQLException e)
        {
            e.getErrorCode();
        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }

    }
    public Event formEvent()
    {
        Event event = new Event();
        event.setYear(0);
        event.setEventType("test");
        event.setCity("test");
        event.setCountry("test");
        event.setLongitude(0.0);
        event.setLatitude(0.0);
        event.setPersonID("test");
        event.setEventID("test");
        event.setDescendant("test");
        return event;
    }
    @Test
    public void insertListOfEvents()
    {


        List<Event> eventsList = new ArrayList<>();
        eventsList.add(formEvent());

        db.start();
        db.eventDao.insertListOfEvents(eventsList);
        Event testEvent = new Event();
        testEvent = db.eventDao.getEventByID("test");
        try
        {
            db.closeConnection(true);
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
        }

        assertEquals(testEvent.eventID, "test");
        assertEquals(testEvent.personID, "test");
        assertEquals(testEvent.descendant, "test");
        assertEquals(testEvent.city, "test");
        assertEquals(testEvent.country, "test");
        assertEquals(testEvent.eventType, "test");
        assertEquals(testEvent.year, 0);
        assertEquals(testEvent.latitude, 0.0);
        assertEquals(testEvent.longitude, 0.0);

        deleteTestEvent();

    }

    @Test
    public void getEventByID()
    {
       List<Event> events = new ArrayList<>();
       events.add(formEvent());
       Event testEvent = new Event();

       db.start();
       try
       {
           db.eventDao.insertListOfEvents(events);
           testEvent =db.eventDao.getEventByID("test");
           db.closeConnection(true);
       }
       catch (DatabaseException e)
       {
           e.getMessage();
       }

        assertEquals(testEvent.eventID, "test");
        assertEquals(testEvent.descendant, "test");
        assertEquals(testEvent.personID, "test");
        assertEquals(testEvent.country, "test");
        assertEquals(testEvent.city, "test");
        assertEquals(testEvent.eventType, "test");
        assertEquals(testEvent.latitude, 0.0);
        assertEquals(testEvent.longitude, 0.0);
        assertEquals(testEvent.year, 0);
    }

}