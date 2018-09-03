package data_access;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.Database;
import model.Person;
import model.User;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
/**
 * Methods to mutate Persons in Database
 */

public class PersonDao
{

    Database db;
    public  List<String> jsonNames;
    public List<Location> jsonLocations;

    public PersonDao(Database db)
    {
        this.db = db;
         importNames();
         importLocations();
    }

    public void generateAncestors(User user, int gens)
    {
        createPersonForUser(user, gens);
    }

    /**
     * This function creates a corresponding persosn for a user
     * @param user
     * @param gens
     */
    public void createPersonForUser(User user, int gens)
    {
        String sql = "insert into persons (personID, descendant, firstName , lastName, gender, father, mother, spouse) values (?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {

            String uuid = formUUID();
            user.setPersonID(uuid);
            db.userDao.updatePersonID(user.username, uuid);

            stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, user.personID);
            stmt.setString(2, user.username);
            stmt.setString(3, user.firstName);
            stmt.setString(4, user.lastName);
            stmt.setString(5, user.gender);
            stmt.setString(6, createFather(gens, user.username));
            stmt.setString(7, createMother(gens, user.username));

            if (stmt.executeUpdate() != 1)
            {
                throw new SQLException();
            }
            if (stmt != null)
            {
                stmt.close();
            }
        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }

    /**
     * This function gets and returns a person based on personID
     * @param personID
     * @return
     */
    public Person getPersonByID(String personID)
    {
        ResultSet resultSet = null;
        Statement stmt = null;
        Person funcPerson = new Person();
        try
        {
            stmt = db.conn.createStatement();
            resultSet = stmt.executeQuery("select * from persons where persons.personID= '" + personID + "'");

            while(resultSet.next())
            {
                funcPerson.setPersonID(resultSet.getString(1));
                funcPerson.setDescendant(resultSet.getString(2));
                funcPerson.setFirstName(resultSet.getString(3));
                funcPerson.setLastName(resultSet.getString(4));
                funcPerson.setGender(resultSet.getString(5));
                funcPerson.setFather(resultSet.getString(6));
                funcPerson.setMother(resultSet.getString(7));
                funcPerson.setSpouse(resultSet.getString(8));
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
            System.out.println("something went wrong in PersonDao getPersonByID" + e.getErrorCode());
        }

        return funcPerson;

    }

    /**
     * This function gets all persons associated with a descendant
     * @param descendant
     * @return
     */
    public List<Person> getAllPersonsFromDescendant(String descendant)
    {
        ResultSet resultSet = null;
        Statement stmt = null;

        List<Person> personList = null;

        try
        {
            stmt = db.conn.createStatement();
            resultSet = stmt.executeQuery("select * from persons where persons.descendant = '" + descendant + "'");
            personList = new ArrayList<>();

            while(resultSet.next())
            {
                Person funcPerson = new Person();
                funcPerson.setPersonID(resultSet.getString(1));
                funcPerson.setDescendant(resultSet.getString(2));
                funcPerson.setFirstName(resultSet.getString(3));
                funcPerson.setLastName(resultSet.getString(4));
                funcPerson.setGender(resultSet.getString(5));
                funcPerson.setFather(resultSet.getString(6));
                funcPerson.setMother(resultSet.getString(7));
                funcPerson.setSpouse(resultSet.getString(8));
                personList.add(funcPerson);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Issue with getAllPersonsFromDescendant in PersonDao");
            e.getErrorCode();
        }



        return personList;
    }

    /**
     * This function gets a personID from an authtoken using a authtoken string
     * @param authToken
     * @return
     */
    public String getPersonIDFromAuthToken(String authToken)
    {
        ResultSet resultSet = null;
        Statement stmt = null;
        String result = null;
        try
        {
            stmt = db.conn.createStatement();
            resultSet = stmt.executeQuery("select username from  authTokens where authTokens.authToken = '" + authToken + "'");

            while(resultSet.next())
            {
                result = resultSet.getString(1);
            }
        }
        catch (SQLException e)
        {
            System.out.println("Issue with getPersonIDFromAuthToken in PersonDao");
            e.getErrorCode();
        }

        return result;

    }

    /**
     * This token creates a person and fills ancestral data
     * @param gens
     * @param descendant
     * @return
     */
    public String createFather(int gens, String descendant)
    {

        if(gens <= 0 )
        {
            return "";
        }
        else
        {
            String sql = "insert into persons (personID, descendant, firstName , lastName, gender, father, mother, spouse) values (?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = null;
            String uuid = formUUID();

            try {

                stmt = db.conn.prepareStatement(sql);
                stmt.setString(1, uuid);
                stmt.setString(2, descendant);
                stmt.setString(3, getRandomName());
                stmt.setString(4, getRandomName());
                stmt.setString(5, "m");
                stmt.setString(6, createFather(gens - 1, descendant));
                stmt.setString(7, createMother(gens - 1, descendant));
                fillRandomEvent(uuid, descendant);
                makeBirthEvent(uuid,descendant);
                makeDeathEvent(uuid,descendant);

                if (stmt.executeUpdate() != 1)
                {
                    throw new SQLException();
                }
                if(stmt != null)
                {

                    stmt.close();
                }

            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
            return uuid;
        }

    }
    /**
     * This token creates a person and fills ancestral data
     * @param gens
     * @param descendant
     * @return
     */
    public String createMother(int gens, String descendant)
    {
        if(gens <= 0 )
        {
            return "";
        }
        else
        {
            String sql = "insert into persons (personID, descendant, firstName , lastName, gender, father, mother, spouse) values (?,?,?,?,?,?,?,?)";
            PreparedStatement stmt = null;
            String uuid = formUUID();

            try {

                stmt = db.conn.prepareStatement(sql);
                stmt.setString(1, uuid);
                stmt.setString(2, descendant);
                stmt.setString(3, getRandomName());
                stmt.setString(4, getRandomName());
                stmt.setString(5, "f");
                stmt.setString(6, createFather(gens - 1, descendant));
                stmt.setString(7, createMother(gens - 1, descendant));
                fillRandomEvent(uuid, descendant);
                makeBirthEvent(uuid,descendant);
                makeDeathEvent(uuid,descendant);

                if (stmt.executeUpdate() != 1)
                {
                    throw new SQLException();
                }
                if(stmt != null)
                {

                    stmt.close();
                }

            }
            catch (SQLException e)
            {
                System.out.println(e.getMessage());
            }
            return uuid;
        }
    }

    private void makeBirthEvent(String uuid, String descendant)
    {
        String sql = "insert into events (eventID ,descendant, personID, latitude, longitude, country, city, eventType, yr) values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        Location location = getRandomLocation();
        try {

            stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, formUUID());
            stmt.setString(2, descendant);
            stmt.setString(3, uuid);
            stmt.setDouble(4, location.latitude);
            stmt.setDouble(5, location.longitude);
            stmt.setString(6, location.country);
            stmt.setString(7, location.city);
            stmt.setString(8, "Birth");
            stmt.setInt(9, getRandomYear());
            if (stmt.executeUpdate() != 1)
            {
                throw new SQLException();
            }


            if (stmt != null)
            {
                stmt.close();
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }
    private void makeDeathEvent(String uuid, String descendant)
    {
        String sql = "insert into events (eventID ,descendant, personID, latitude, longitude, country, city, eventType, yr) values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        Location location = getRandomLocation();
        try {

            stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, formUUID());
            stmt.setString(2, descendant);
            stmt.setString(3, uuid);
            stmt.setDouble(4, location.latitude);
            stmt.setDouble(5, location.longitude);
            stmt.setString(6, location.country);
            stmt.setString(7, location.city);
            stmt.setString(8, "Death");
            stmt.setInt(9, getRandomYear());
            if (stmt.executeUpdate() != 1)
            {
                throw new SQLException();
            }


            if (stmt != null)
            {
                stmt.close();
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }


    /**
     * This function gets a user from a authToken
     */
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
        }
        catch (SQLException e)
        {
            System.out.println("something went wrong in PersonDao getByUserName" + e.getErrorCode());
        }
        finally
        {
            if(stmt != null)
            {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if(resultSet != null)
            {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return funcUser;

    }


    /**
     * This function forms a uuid
     * @return
     */
    public static String formUUID()
    {
        String token  = UUID.randomUUID().toString();
        return token;
    }

    /**
     * This function gets a random name from the list of names
     * @return
     */
    public String getRandomName()
    {
        Random rand = new Random();
        int n = rand.nextInt(jsonNames.size());
        return jsonNames.get(n);
    }
    /**
     * This function gets a random location from the list of locations
     * @return
     */
    public Location getRandomLocation()
    {
        Random rand = new Random();
        int n = rand.nextInt(jsonLocations.size());
        return jsonLocations.get(n);
    }

    /**
     * This function returns a random year between 1500 and 2018
     * @return
     */
    public int getRandomYear()
    {
        Random rand = new Random();
        return rand.nextInt(2018) + 1500;
    }

    /**
     * This Function gets a random eventType from a short list of events
     * @return
     */
    public String getRandomEvent()
    {
        String[] events = {
                "Walked the dog",
                "Walked the cat",
                "Saw aliens",
                "Saw Fred",
                "Got groceries",
                "Finished project",
                "Birth",
                "Death",
                "Ate soup",
                "Drank Coke",
                "Played games",
                "Played Rocket League with Luke",
                "Danced with Ellie",
                "Ate Dinner"
        };

        Random rand = new Random();
        return events[rand.nextInt(events.length)];
    }

    /**
     * This function creates and inserts a random event int the database
     * @param personID
     */
    public void fillRandomEvent(String personID, String descendant)
    {
        String sql = "insert into events (eventID ,descendant, personID, latitude, longitude, country, city, eventType, yr) values (?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        Location location = getRandomLocation();
        try {

                stmt = db.conn.prepareStatement(sql);
                stmt.setString(1, formUUID());
                stmt.setString(2, descendant);
                stmt.setString(3, personID);
                stmt.setDouble(4, location.latitude);
                stmt.setDouble(5, location.longitude);
                stmt.setString(6, location.country);
                stmt.setString(7, location.city);
                stmt.setString(8, getRandomEvent());
                stmt.setInt(9, getRandomYear());
                if (stmt.executeUpdate() != 1)
                {
                    throw new SQLException();
                }


            if (stmt != null)
            {
                stmt.close();
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }

    /**
     * This function imports names from the given json files
     */
    public void importNames()
    {

        String[] fileNames = { "./json/mnames.json","./json/fnames.json","./json/snames.json"};
        JsonParser parser = new JsonParser();
        try
        {
            jsonNames = new ArrayList<>();
            for(String fileName : fileNames)
            {
                Object obj = parser.parse(new FileReader(fileName));
                JsonObject jsonObject = (JsonObject) obj;
                JsonArray names = (JsonArray) jsonObject.get("data");
                for(JsonElement name : names)
                {
                    String n = name.getAsString();
                    jsonNames.add(n);
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * This function imports locations from the given json file
     */
    public void importLocations()
    {
        String fileName = "./json/locations.json";
        JsonParser parser = new JsonParser();
        try
        {
            Object obj = parser.parse(new FileReader(fileName));
            JsonObject jsonObject = (JsonObject) obj;
            JsonArray locations = (JsonArray) jsonObject.get("data");

            jsonLocations = new ArrayList<>();
            for(JsonElement locationObj : locations)
            {
               JsonObject jLocation = locationObj.getAsJsonObject();
               Location loc  = new Location();
               loc.country   = jLocation.get("country").getAsString();
               loc.city      = jLocation.get("city").getAsString();
               loc.latitude  = jLocation.get("latitude").getAsDouble();
               loc.longitude = jLocation.get("longitude").getAsDouble();
               jsonLocations.add(loc);
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }

    }
    public class Location
    {
       public String country;
       public String city;
       public Double latitude;
       public Double longitude;
    }

    /**
     * This function inserts a list of persons into the database
     * @param persons
     * @return
     */
    public boolean insertListOfPersons(List<Person> persons) {
        boolean result = true;
        String sql = "insert into persons (personID, descendant, firstName , lastName, gender, father, mother, spouse) values (?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {

            for (Person p : persons) {

                stmt = db.conn.prepareStatement(sql);
                stmt.setString(1, p.personID);
                stmt.setString(2, p.descendant);
                stmt.setString(3, p.firstName);
                stmt.setString(4, p.lastName);
                stmt.setString(5, p.gender);
                stmt.setString(6, p.father);
                stmt.setString(7, p.mother);
                stmt.setString(8, p.spouse);
                if (stmt.executeUpdate() != 1)
                {
                    result = false;
                    throw new SQLException();
                }
            }

            if (stmt != null) {
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

}
