package test;

import database.Database;
import database.DatabaseException;
import model.Person;
import model.User;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static junit.framework.TestCase.assertEquals;

public class PersonDaoTest {
    Database db = new Database();

    @Test
    public void createPersonForUser()
    {
        db.start();
        Person person = new Person();
        try
        {
            db.personDao.createPersonForUser(formUser(),0);
            person = getPersonByName("test");
            db.closeConnection(true);
        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }

        assertEquals(person.firstName ,"test");
        assertEquals(person.lastName  ,"test");
        assertEquals(person.gender    ,"test");

    }
    public User formUser()
    {

            User user  = new User();
            user.username = "test";
            user.authToken = "test";
            user.lastName = "test";
            user.firstName = "test";
            user.gender="test";
            user.lastName ="test";
            user.email = "test";
            user.password = "test";
            user.personID = "test";
            return user;
    }
    @Test
    public void getPersonByID()
    {
        Person person = new Person();
        db.start();
        try
        {
            createTestPerson(formUser());
            person = db.personDao.getPersonByID("test");
            db.closeConnection(true);
        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }

        assertEquals(person.personID  ,"test");
        assertEquals(person.descendant,"test");
        assertEquals(person.firstName ,"test");
        assertEquals(person.lastName  ,"test");
        assertEquals(person.gender    ,"test");
        assertEquals(person.father    ,"test");
        assertEquals(person.mother    ,"test");
        assertEquals(person.spouse    ,"test");

    }
    public void createTestPerson(User user)
    {
        String sql = "insert into persons (personID, descendant, firstName , lastName, gender, father, mother, spouse) values (?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = null;
        try {


            stmt = db.conn.prepareStatement(sql);
            stmt.setString(1, "test");
            stmt.setString(2, "test");
            stmt.setString(3, "test");
            stmt.setString(4, "test");
            stmt.setString(5, "test");
            stmt.setString(6, "test");
            stmt.setString(7, "test");
            stmt.setString(8, "test");


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
    public Person getPersonByName(String name)
    {
        ResultSet resultSet = null;
        Statement stmt = null;
        Person funcPerson = new Person();
        try
        {
            stmt = db.conn.createStatement();
            resultSet = stmt.executeQuery("select * from persons where persons.firstName = '" + name + "'");

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

}