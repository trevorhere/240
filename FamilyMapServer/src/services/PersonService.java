package services;

import database.Database;
import database.DatabaseException;
import model.AuthToken;
import model.Person;
import model.User;
import request.PersonRequest;
import response.PersonResponse;

import java.util.List;


public class PersonService {

    PersonResponse personResponse = new PersonResponse();
    Database db = new Database();

    public String getPeopleFromUser(PersonRequest pr)
    {
        db.start();
        List<Person> personList = null;
        User funcUser = null;
        String result;

        //authtoke - > personID -> all people with descendant = personID

        funcUser = db.personDao.getUserFromToken(pr.authToken);
        personList = db.personDao.getAllPersonsFromDescendant(funcUser.username);

        result = personResponse.fetchAllPeopleSuccessResponse(personList);

        try
        {
            db.closeConnection(true);
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
            result = personResponse.errorResponse("Internal server error");
        }

        return result;

    }
    public String getPersonByID(PersonRequest pr)
    {
        String AuthToken = pr.authToken;
        String personID = pr.personID;
        String result = null;
        String currUserPersonID = null;
        Person funcPerson;

        db.start();

        try {

            funcPerson = db.personDao.getPersonByID(personID);

                currUserPersonID = db.personDao.getPersonIDFromAuthToken(AuthToken);

                if(funcPerson.descendant != null)
                {

                    if (funcPerson.descendant.equals(currUserPersonID) && currUserPersonID != null)
                    {
                        result = personResponse.getPersonSuccessResponse(funcPerson);
                    }
                    else
                    {
                        result = personResponse.errorResponse("Requested person does not belong to this user");
                    }


            }
            else
            {
                result = personResponse.errorResponse("Invalid PersonID");
            }


                db.closeConnection(true);


        }
        catch (Exception e)
        {
            e.getMessage();
        }
        catch (DatabaseException e) {
            e.printStackTrace();
            result = personResponse.errorResponse("Internal server error");
        }

        return result;

    }
    public boolean authenticateAuthToken(String authToken)
    {
        db.start();
        boolean result = false;
        AuthToken token = new AuthToken();

        result = db.authTokenDao.authenticateToken(authToken);

        try
        {
            db.closeConnection(true);
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();

        }

        return result;
    }

}
