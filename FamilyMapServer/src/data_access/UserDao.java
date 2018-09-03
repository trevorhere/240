package data_access;

import database.Database;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.UUID;

/**
 * Methods to mutate Users in Database
 */
public class UserDao
{

    public static Database db;

    public UserDao(Database db)
    {
        this.db = db;
    }

    /**
     * This function returns true if User exists in the database, false otherwise.
     * @param user
     * @return
     * @throws SQLException
     */
    public boolean authenticateUser(User user) throws SQLException
    {
        boolean result = false;

        User funcUser =  new User(getUserByUsername(user.username));

        if(user.password.equals(funcUser.password))
        {
            result = true;
            setAuthToken(user.username);
        }
        return result;
    }

    /**
     * This function gets a user from the database based on their username
     * @param username
     * @return User
     * @throws SQLException
     */
    public static User getUserByUsername(String username) throws SQLException
    {
        ResultSet resultSet = null;
        Statement stmt = null;
        User funcUser = new User();

        try
        {
            stmt = db.conn.createStatement();
            resultSet = stmt.executeQuery("select * from users where users.userName = '" + username + "'");

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
            System.out.println("something went wrong in UserDao getByUserName" + e.getErrorCode());
        }
        finally
        {
            if(stmt != null)
            {
                stmt.close();
            }
            if(resultSet != null)
            {
                resultSet.close();
            }
        }
        return funcUser;
    }

    /**
     * This function sets a new authtoken in the database based on username
     * @param username
     */
    public void setAuthToken(String username)
    {
        Statement stmt = null;

        try
        {
            stmt = db.conn.createStatement();
            stmt.executeUpdate("update users set authToken  = '" + formAuthToken() + "' where users.userName = '" + username + "'");
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("something went wrong in UserDao setAuthToken");
        }
        finally
        {
            if(stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Ths function updates the personID of a user in the database
     * @param username
     * @param uuid
     */
    public void updatePersonID(String username, String uuid)
    {
        Statement stmt = null;
        try
        {
            stmt = db.conn.createStatement();
            stmt.executeUpdate("update users set personID = '" + uuid + "' where users.userName = '" + username + "'");

            if(stmt != null)
            {
                stmt.close();
            }

        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.out.println("something went wrong in UserDao updatePersonID");
        }
    }

    /**
     * This function inserts a new user in the database
     * @param user
     * @return true if successful, false otherwise
     */
    public static boolean registerUser(User user)
    {

    boolean result = true;
    String sql = "insert into users (userName, email, password, firstName, lastName, gender, personID, authToken) values (?,?,?,?,?,?,?,?)";
    PreparedStatement stmt = null;
    try {
             stmt = db.conn.prepareStatement(sql);
             stmt.setString(1, user.username);
             stmt.setString(2, user.email);
             stmt.setString(3, user.password);
             stmt.setString(4, user.firstName);
             stmt.setString(5, user.lastName);
             stmt.setString(6, user.gender);
             stmt.setString(7, user.username);
             stmt.setString(8, formAuthToken());

             if (stmt.executeUpdate() != 1)
             {
                   result = false;
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
         return result;
    }
    public static String formAuthToken()
    {
      String token  = UUID.randomUUID().toString();
      return token;
    }

    /**
     * This function inserts a list of users in the database
     * @param users
     * @return true if successful, false otherwise
     */
    public boolean insertListOfUsers(List<User> users)
    {
        boolean result = true;
        String sql = "insert into users (userName, email, password, firstName, lastName, gender, personID, authToken) values (?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {

            for(User u : users)
            {
                stmt = db.conn.prepareStatement(sql);
                stmt.setString(1, u.username);
                stmt.setString(2, u.email);
                stmt.setString(3, u.password);
                stmt.setString(4, u.firstName);
                stmt.setString(5, u.lastName);
                stmt.setString(6, u.gender);
                stmt.setString(7, u.personID);
                stmt.setString(8, formAuthToken());
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
            e.getMessage();
        }
        return result;
    }

    /**
     * This function checks to see if a username already exist in the database
     * @param username
     * @return true if username exists, false otherwise
     */
    public boolean checkIfUsernameIsTaken(String username)
    {
        boolean result = false;
        try
        {
            User user = getUserByUsername(username);
            if(user.username != null)
            {
                result = true;
            }
        }
        catch (SQLException e)
        {
            e.getErrorCode();
        }

        return result;
    }

    /**
     * This function fills user data with random data
     * @param username
     * @param generations
     * @return true if successful, false otherwise
     */
    public String fillUser(String username, int generations)
    {
        Statement deleteEvents = null;
        Statement deletePeople = null;

        String result = "Internal server error";
        try
        {
            User funcUser;
            funcUser  = getUserByUsername(username);


            deletePeople = db.conn.createStatement();
            deleteEvents = db.conn.createStatement();
            deleteEvents.executeUpdate("delete from events where events.descendant = '" +  funcUser.username + "'");
            deletePeople.executeUpdate("delete from persons where persons.descendant = '" +  funcUser.username + "'");


            if(registerUser(funcUser))
            {

                db.personDao.createPersonForUser(funcUser,generations);
                if(generations <= 0)
                {
                    result = "Successfully inserted user";
                }
                else
                {
                    result = "Successfully inserted user and " + generations + " generations";
                }
            }

        }
        catch (SQLException e)
        {
            System.out.println("something went wrong in EventDao getEventByID" + e.getErrorCode());
            result = "User was not successfully inserted";
        }

        return result;
    }
}
