package services;

import database.Database;
import database.DatabaseException;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import response.LoginResponse;

import java.sql.SQLException;

public class LoginService
{
   LoginResponse lr = new LoginResponse();
   Database db = new Database();

   public String login(LoginRequest r)
    {

        db.start();
        boolean result = false;
        User funcUser =  new User(r.getUser());
        String response;

        try
        {
            result = db.userDao.authenticateUser(funcUser);
            if(result)
            {
                funcUser = db.userDao.getUserByUsername(funcUser.username);

                AuthToken token = new AuthToken(funcUser.username, funcUser.authToken, funcUser.personID);
                db.authTokenDao.storeAuthToken(token);

            }

            db.closeConnection(true);
        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }
        catch (SQLException e)
        {
        e.printStackTrace();
        }
        catch (Exception e)
        {
        System.out.println("Issue with Login in Login Service");
        }

        if(result)
        {
            response =  lr.successResponse(funcUser.authToken, funcUser.username, funcUser.personID);
        }
        else
        {
            response =  lr.errorResponse("username or password is incorrect.");
        }

        return response;
    }
}

