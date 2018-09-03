package data_access;

import database.Database;
import model.Event;
import model.Person;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/**
 * Methods to mutate Events in Database
 */
public class EventDao
{

    public static Database db;

    public EventDao(Database db)
    {
        this.db = db;
    }

    public User getUserFromToken(String authToken)
    {

        ResultSet resultSet = null;
        Statement stmt = null;
        User funcUser = new User();

        try
        {
            stmt = db.conn.createStatement();
            resultSet = stmt.executeQuery("select * from users where users.authToken = '" + authToken + "'");

            while(resultSet.next())
            {
                funcUser.setUsername(resultSet.getString(1));
                funcUser.setPassword(resultSet.getString(2));
                funcUser.setEmail(resultSet.getString(3));
                funcUser.setFirstName(resultSet.getString(4));
                funcUser.setLastName(resultSet.getString(5));
                funcUser.setGender(resultSet.getString(6));
                funcUser.setPersonID(resultSet.getString(7));
                funcUser.setAuthToken(resultSet.getString(8));
            }

            if(stmt != null)
            {
                stmt.close();
            }
            if(resultSet != null)
            {
                resultSet.close();
            }
        }
        catch (SQLException e)
        {
            System.out.println("something went wrong in UserDao getByUserName" + e.getErrorCode());
        }

        return funcUser;
    }
    public boolean insertListOfEvents(List<Event> events) {

        boolean result = true;
        String sql = "insert into events (eventID ,descendant, personID, latitude, longitude, country, city, eventType, yr) values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {

            for (Event e : events)
            {
                stmt = db.conn.prepareStatement(sql);
                stmt.setString(1, e.eventID);
                stmt.setString(2, e.descendant);
                stmt.setString(3, e.personID);
                stmt.setDouble(4, e.latitude);
                stmt.setDouble(5, e.longitude);
                stmt.setString(6, e.country);
                stmt.setString(7, e.city);
                stmt.setString(8, e.eventType);
                stmt.setInt(9, e.year);
                if (stmt.executeUpdate() != 1)
                {
                    result = false;
                    throw new SQLException();
                }
            }

            if (stmt != null)
            {
                stmt.close();
            }


        }
        catch (SQLException e)
        {
            result = false;
            System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            result = false;
            System.out.println(e.getMessage());
        }

        return result;

    }
    public static String formUUID()
    {
        String token  = UUID.randomUUID().toString();
        return token;
    }

    public Event getEventByID(String eventID)
    {
        ResultSet resultSet = null;
        Statement stmt = null;
        Event funcEvent = new Event();
        try
        {
            stmt = db.conn.createStatement();
            resultSet = stmt.executeQuery("select * from events where events.eventID = '" + eventID + "'");

            while(resultSet.next())
            {
                funcEvent.setEventID(resultSet.getString(1));
                funcEvent.setDescendant(resultSet.getString(2));
                funcEvent.setPersonID(resultSet.getString(3));
                funcEvent.setLatitude(resultSet.getDouble(4));
                funcEvent.setLongitude(resultSet.getDouble(5));
                funcEvent.setCountry(resultSet.getString(6));
                funcEvent.setCity(resultSet.getString(7));
                funcEvent.setEventType(resultSet.getString(8));
                funcEvent.setYear(resultSet.getInt(9));
            }

            if(stmt != null)
            {
                stmt.close();
            }
            if(resultSet != null)
            {
                resultSet.close();
            }
        }
        catch (SQLException e)
        {
            System.out.println("something went wrong in EventDao getEventByID" + e.getErrorCode());
        }
        return funcEvent;
    }

    public List<Event> getAllEventsFromPersonList(List<Person> personList)
    {
        ResultSet resultSet = null;
        Statement stmt = null;

        List<Event> eventList = new ArrayList<>();

        try
        {
            for(Person p : personList)
            {
                stmt = db.conn.createStatement();
                resultSet = stmt.executeQuery("select * from events where events.descendant = '" + p.descendant + "'");

                while(resultSet.next())
                {
                    Event funcEvent= new Event();
                    funcEvent.setEventID(resultSet.getString(1));
                    funcEvent.setDescendant(resultSet.getString(2));
                    funcEvent.setPersonID(resultSet.getString(3));
                    funcEvent.setLatitude(resultSet.getDouble(4));
                    funcEvent.setLongitude(resultSet.getDouble(5));
                    funcEvent.setCountry(resultSet.getString(6));
                    funcEvent.setCity(resultSet.getString(7));
                    funcEvent.setEventType(resultSet.getString(8));
                    funcEvent.setYear(resultSet.getInt(9));
                    eventList.add(funcEvent);
                }
            }
        }
        catch (SQLException e)
        {
            System.out.println("Issue with getAllEventsFromPersonList in eventDao");
            e.getErrorCode();
        }



        return eventList;
    }
}
