package services;

import database.Database;
import database.DatabaseException;
import model.Event;
import model.Person;
import model.User;
import request.EventRequest;
import response.EventResponse;

import java.util.List;


public class EventService {

    EventResponse eventResponse = new EventResponse();
    Database db = new Database();

    public boolean getUserFromToken(String authToken)
    {
        db.start();
        boolean result = false;

        try
        {
            User funcUser = new User(db.eventDao.getUserFromToken(authToken));
            db.closeConnection(true);

            if(funcUser != null)
                result = true;


        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }


        return result;
    }

    public String getEventsFromUser(EventRequest eventRequest)
    {
        db.start();
        List<Event> eventList = null;
        List<Person> personList= null;
        User funcUser = null;
        String result;

        //authtoke - > personID -> all people with descendant -> all events that belong to all these people
        try
        {

        funcUser = db.personDao.getUserFromToken(eventRequest.authToken);
        personList = db.personDao.getAllPersonsFromDescendant(funcUser.username);
        eventList = db.eventDao.getAllEventsFromPersonList(personList);
        result = eventResponse.fetchAllEventsSuccessResponse(eventList);
        db.closeConnection(true);

        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
            result = eventResponse.errorResponse("Internal server error");
        }

        return result;
    }

    public String getEventByID(EventRequest er)
    {
        String AuthToken = er.authToken;
        String eventID = er.eventID;
        String result = null;
        String currUserPersonID = null;
        Event funcEvent;

        db.start();

        try {

            funcEvent = db.eventDao.getEventByID(eventID);
            currUserPersonID = db.personDao.getPersonIDFromAuthToken(AuthToken);

            if(funcEvent.eventID != null)
            {

                if (funcEvent.personID.equals(currUserPersonID) && currUserPersonID != null)
                {
                    result = eventResponse.getEventSuccessResponse(funcEvent);
                }
                else
                {
                    result = eventResponse.errorResponse("Requested event does not belong to this user");
                }
            }
            else
            {
                result = eventResponse.errorResponse("Invalid eventID");
            }
            db.closeConnection(true);
        }
        catch (Exception e)
        {
            e.getMessage();
        }
        catch (DatabaseException e) {
            e.printStackTrace();
            result = eventResponse.errorResponse("Internal server error");
        }

        return result;
    }


    public class EventResult
    {
        String message;
    }

}
