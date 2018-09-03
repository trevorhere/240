package services;

import database.Database;
import database.DatabaseException;
import request.FillRequest;
import response.FillResponse;

/**
 * Fill Service
 * Populates the server's database with generated data for the specified user name.
 */
public class FillService {

    FillResponse fillResponse = new FillResponse();
    Database db = new Database();
    String result;

    public String fillFromUserName(FillRequest fillRequest)
    {
        db.start();
        try
        {
            if(db.userDao.checkIfUsernameIsTaken(fillRequest.username))
            {
                int generations = 4;
                result = fillResponse.successResponse(db.userDao.fillUser(fillRequest.username, generations));

            }
            else
            {
                result = fillResponse.errorResponse("Username does not exists");
            }

           db.closeConnection(true);
        }
        catch (DatabaseException e)
        {
            e.getMessage();
            result = fillResponse.errorResponse("Internal server error");
        }
        return result;
    }

    public String fillFromUserNameAddGenerations(FillRequest fillRequest)
    {
        db.start();
        try
        {
            if(db.userDao.checkIfUsernameIsTaken(fillRequest.username))
            {
                result = fillResponse.successResponse(db.userDao.fillUser(fillRequest.username, fillRequest.generations));
            }
            else
            {
                result = fillResponse.errorResponse("Username does not exists");
            }
            db.closeConnection(true);

            return result;
        }
        catch (DatabaseException e)
        {
            e.getMessage();
            result = fillResponse.errorResponse("Internal server error");
        }
        return result;
    }

}
