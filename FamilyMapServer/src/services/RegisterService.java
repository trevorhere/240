package services;

import database.Database;
import database.DatabaseException;
import model.AuthToken;
import model.User;
import request.RegisterRequest;
import response.RegisterResponse;

import java.sql.SQLException;

/**
 * RegisterService
 * Creates a new user account, generates 4 generations of ancestor data for the new
 * user, logs the user in, and returns an auth token.
 */
public class RegisterService {

    RegisterResponse rr = new RegisterResponse();
    Database db = new Database();


    public String register(RegisterRequest r)
    {
        db.start();
        boolean result = false;
        User funcUser = new User(r.getUser());
        String response;


        try
        {
            result = db.userDao.registerUser(funcUser);
             if(result)
             {
                 db.personDao.generateAncestors(funcUser,4);
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
            e.getErrorCode();
        }

            if(result)
            {
                response = rr.successResponse(funcUser.authToken, funcUser.username, funcUser.personID);
            }
            else
            {
                //  registerResponse.ErrorResponse("Error with User Registration" , exchange);
                response = rr.errorResponse("Error with User registration");
            }

            return response;
    }

    public boolean checkIfUsernameIsTaken(String username)
    {
        db.start();
        boolean result = true;
        try
        {
           result  = db.userDao.checkIfUsernameIsTaken(username);
           db.closeConnection(true);
           return result;
        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }

        return result;
    }
}



