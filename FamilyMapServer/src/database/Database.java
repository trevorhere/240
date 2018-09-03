package database;


import data_access.AuthTokenDao;
import data_access.EventDao;
import data_access.PersonDao;
import data_access.UserDao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    public UserDao userDao = new UserDao(this);
    public EventDao eventDao = new EventDao(this);
    public PersonDao personDao = new PersonDao(this);
    public AuthTokenDao authTokenDao = new AuthTokenDao(this);



    static {
        try {
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            System.out.println("Class Not Found");
            e.printStackTrace();
        }
    }


    public Connection conn;

    public void openConnection() throws DatabaseException {
        try {
            final String CONNECTION_URL = "jdbc:sqlite:db.sqlite";

            // Open a database connection
            conn = DriverManager.getConnection(CONNECTION_URL);
            createTables();


            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new DatabaseException("openConnection failed", e);
        }
    }

    public void start() {

        try {
            openConnection();
        } catch (DatabaseException e) {
            System.out.println("Issue with start() in Database");
            e.printStackTrace();
        }

    }

    public void closeConnection(boolean commit) throws DatabaseException {
        try {
            if (commit) {
                conn.commit();
            } else {
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            throw new DatabaseException("closeConnection failed", e);
        }
    }

    public void createTables()
    {

            createUsersTable();
            createEventsTable();
            createPersonsTable();
            createAuthTokensTable();

    }
    public void clearDataBase()
    {
        resetUsersTable();
        resetEventsTable();
        resetPersonsTable();
        resetAuthTokensTable();

    }
    public void createAuthTokensTable()
    {
        try
        {
            Statement createAuthTokens = null;
            try
            {
                createAuthTokens = conn.createStatement();
                createAuthTokens.executeUpdate(
                        " create table if not exists authTokens ( " +
             " userName  text not null , authToken text not null, personID  text not null )");
            }
            finally
            {
                if (createAuthTokens != null)
                {
                    createAuthTokens.close();

                }
            }

        }
        catch(SQLException e)
        {
            e.getErrorCode();
        }
    }

    public void createEventsTable()
    {
        try
        {
            Statement createEvents = null;
            try {
                createEvents = conn.createStatement();
                createEvents.executeUpdate(
                        " create table if not exists events ( " +
                                " eventID text not null unique primary key, " +
                                " descendant text       , " +
                                " personID   text       , " +
                                " latitude double       , " +
                                " longitude text double , " +
                                " country text          , " +
                                " city text              , " +
                                " eventType text        , " +
                                " yr int                  " +
                                ")");
            }
            finally
            {
                if (createEvents != null)
                {
                    createEvents.close();

                }
            }

        }
        catch(SQLException e)
        {
            e.getErrorCode();
        }
    }
    public void createPersonsTable() {
        try {
            Statement createPersons = null;
            try {
                createPersons = conn.createStatement();
                createPersons.executeUpdate(
                        " create table if not exists persons (  " +
                                " personID    text UNIQUE PRIMARY KEY not null , " +
                                " descendant  text                  , " +
                                " firstName   text not null         , " +
                                " lastName    text not null         , " +
                                " gender      text                  , " +
                                " father      text                  , " +
                                " mother      text                  , " +
                                " spouse      text                  ) "
                );
            } finally {
                if (createPersons != null) {
                    createPersons.close();

                }
            }

        } catch (SQLException e) {
            e.getErrorCode();
        }
    }
    public void createUsersTable()
    {
        try
        {
            Statement createUsers = null;
            try
            {
                createUsers = conn.createStatement();
                createUsers.executeUpdate(
                        " create table if not exists users ( " +
                                " userName  text not null, " +
                                " password  text not null, " +
                                " email     text not null, " +
                                " firstName text not null, " +
                                " lastName  text not null, " +
                                " gender    text         , " +
                                " personID  text         , " +
                                " authToken text           " +
                                ")");
            }
            finally
            {
                if (createUsers != null)
                {
                    createUsers.close();

                }
            }

        }
        catch(SQLException e)
        {
            e.getErrorCode();
        }
    }
    public void resetAuthTokensTable()
    {
        Statement dropAuthTokens = null;
        try
        {
            dropAuthTokens = conn.createStatement();

            dropAuthTokens.executeUpdate( "drop table if exists authTokens");
            createAuthTokensTable();
        }
        catch (SQLException e)
        {
            e.getSQLState();
        }
    }
    public void resetUsersTable()
    {
        Statement dropUsers = null;
        try
        {
            dropUsers = conn.createStatement();

            dropUsers.executeUpdate( "drop table if exists users");
            createUsersTable();
        }
        catch (SQLException e)
        {
            e.getSQLState();
        }
    }
    public void resetEventsTable()
    {
        Statement dropEvents = null;
        try
        {
            dropEvents= conn.createStatement();

            dropEvents.executeUpdate( "drop table if exists events");
            createEventsTable();
        }
        catch (SQLException e)
        {
            e.getSQLState();
        }
    }
    public void resetPersonsTable()
    {
        Statement dropPersons= null;
        try
        {
            dropPersons = conn.createStatement();

            dropPersons.executeUpdate( "drop table if exists persons");
            createPersonsTable();
        }
        catch (SQLException e)
        {
            e.getSQLState();
        }
     }

    }


