package test;

import database.Database;
import database.DatabaseException;
import model.User;
import org.junit.Test;
import request.LoginRequest;
import services.LoginService;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import static junit.framework.TestCase.assertEquals;

public class LoginServiceTest {
    Database db = new Database();

    @Test
    public void login()
    {
        LoginRequest loginRequest = FormLoginRequest();
        LoginService ls = new LoginService();

        String[] lrResponse = ls.login(loginRequest).split("\"");

        assertEquals(lrResponse[7],"test");
        assertEquals(lrResponse[11],"test");


    }
    public LoginRequest FormLoginRequest()
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

        registerUser();
        LoginRequest lr = new LoginRequest(user);
        return lr;

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
}