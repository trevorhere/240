package services;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import database.Database;
import database.DatabaseException;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import response.LoadResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * LoadService
 * Clears all data from database, then loads the posted user,
 * person and event data into data base
 */
public class LoadService {

    LoadResponse loadResponse = new LoadResponse();
    Database db = new Database();
    int eventSize;
    int userSize;
    int personSize;
    String result;
    boolean usersGood = false;
    boolean personsGood = false;
    boolean eventsGood = false;

    public String load(LoadRequest lr)
    {

        db.start();
        try
        {

          usersGood = db.userDao.insertListOfUsers(getUserDataFromLoadRequest(lr.json));
          personsGood = db.personDao.insertListOfPersons(getPersonDataFromLoadRequest(lr.json));
          eventsGood  = db.eventDao.insertListOfEvents(getEventDataFromLoadRequest(lr.json));

          db.closeConnection(true);

          if(usersGood == true && eventsGood == true && personsGood == true)
          {
              result = loadResponse.successResponse(userSize, personSize, eventSize);
          }
          else
          {
              result = loadResponse.errorResponse("Error with input data");
          }

        }
        catch (Exception e)
        {
            e.getMessage();
            result = loadResponse.errorResponse("Error with input data");
        }
        catch (DatabaseException e)
        {
            e.getMessage();
            result = loadResponse.errorResponse("Internal server error");
        }

        return result;
    }
    public List<User> getUserDataFromLoadRequest(JsonObject json)
    {
        JsonArray userJson;
        List<User> users =  new ArrayList<>();
        try {

            if (json != null && json.has("users")) {
                userJson = json.getAsJsonArray("users");
                for (JsonElement e : userJson) {
                    JsonObject eUser = e.getAsJsonObject();
                    User user = new User();
                    user.setUsername(eUser.get("userName").getAsString());
                    user.setPassword(eUser.get("password").getAsString());
                    user.setEmail(eUser.get("email").getAsString());
                    user.setFirstName(eUser.get("firstName").getAsString());
                    user.setLastName(eUser.get("lastName").getAsString());
                    user.setGender(eUser.get("gender").getAsString());
                    user.setPersonID(eUser.get("personID").getAsString());
                    users.add(user);
                }
            }
        }
    catch (Exception e)
            {
                System.out.println(e.getMessage());
                result = "Issue with user data";
            }

        userSize = users.size();
        return users;
    }
    public List<Person> getPersonDataFromLoadRequest(JsonObject json)
    {
        JsonArray personJson;
        List<Person> persons = new ArrayList<>();
        try
        {
            if (json != null && json.has("persons"))
            {
                personJson =  json.getAsJsonArray("persons");
                for(JsonElement e : personJson)
                {
                    JsonObject ePerson = e.getAsJsonObject();
                    Person person = new Person();
                    person.setFirstName  (ePerson.get("firstName").getAsString());
                    person.setLastName   (ePerson.get("lastName").getAsString());
                    person.setGender     (ePerson.get("gender").getAsString());
                    person.setPersonID   (ePerson.get("personID").getAsString());
                    if(ePerson.has("father"))
                    {
                        person.setFather(ePerson.get("father").getAsString());
                    }
                    if(ePerson.has("mother"))
                    {
                        person.setMother(ePerson.get("mother").getAsString());
                    }
                    if(ePerson.has("spouse"))
                    {
                        person.setSpouse(ePerson.get("spouse").getAsString());
                    }
                    if(ePerson.has("descendant"))
                    {
                        person.setDescendant(ePerson.get("descendant").getAsString());
                    }
                    persons.add(person);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            result = "Issue with person data";
        }
        personSize = persons.size();
        return persons;

    }
    public List<Event> getEventDataFromLoadRequest(JsonObject json)
    {
        JsonArray eventJson;

        List<Event> events = new ArrayList<>();
        try
        {

           if (json != null && json.has("events"))
            {
                eventJson =  json.getAsJsonArray("events");
                for(JsonElement e : eventJson)
                {
                    JsonObject eEvent = e.getAsJsonObject();
                    Event event = new Event();
                    event.setEventID   (eEvent.get("eventID").getAsString());
                    event.setEventType (eEvent.get("eventType").getAsString());
                    event.setPersonID  (eEvent.get("personID").getAsString());
                    event.setCity      (eEvent.get("city").getAsString());
                    event.setCountry   (eEvent.get("country").getAsString());
                    event.setLatitude  (eEvent.get("latitude").getAsDouble());
                    event.setLongitude (eEvent.get("longitude").getAsDouble());
                    event.setYear      (eEvent.get("year").getAsInt());
                    event.setDescendant(eEvent.get("descendant").getAsString());
                    events.add(event);
                }
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            result = "Issue with event data";
        }
        eventSize = events.size();
        return events;
    }
}
