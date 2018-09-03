package response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Event;

import java.util.List;

/**
 * EventResponse consists of decendant, eventID, personID, latitude, longitude, country, city, eventType and year.
 *
 * <Pre><b>Domain: </b>
 *   decendant :String
 *   eventID  :String
 *   personID :String
 *   latitued :Integer
 *   longitude :Integer
 *   country  :String
 *   city    :String
 *   eventType :String
 *   year    :Integer
 * </Pre>
 *
 */
public class EventResponse {

    Gson gson  = new Gson();
    JsonObject returnObj = new JsonObject();
    JsonArray eventArray = new JsonArray();



    public String getEventSuccessResponse(Event e)
    {

        returnObj.addProperty("descendant",    e.descendant);
        returnObj.addProperty("personID",      e.personID);
        returnObj.addProperty("eventID",       e.eventID);
        returnObj.addProperty("latitude",      e.latitude);
        returnObj.addProperty("longitude",     e.longitude);
        returnObj.addProperty("country",       e.country);
        returnObj.addProperty("city",          e.city);
        returnObj.addProperty("eventType",     e.eventType);
        returnObj.addProperty("year",          e.year);
        return gson.toJson(returnObj);
    }
    public String errorResponse(String message)
    {
        returnObj.addProperty("Message", message);
        return gson.toJson(returnObj);
    }


    public String fetchAllEventsSuccessResponse(List<Event> eventList)
    {
        for(Event e : eventList)
        {
            JsonObject event = new JsonObject();
           event.addProperty("eventId",    e.eventID);
           event.addProperty("descendant", e.descendant);
           event.addProperty("personID",   e.personID);
           event.addProperty("latitude",   e.latitude);
           event.addProperty("longitude",  e.longitude);
           event.addProperty("country",    e.country);
           event.addProperty("city",       e.city);
           event.addProperty("eventType",  e.eventType);
           event.addProperty("year",       e.year);
            eventArray.add(event);
        }

        returnObj.add("data", eventArray);
        return gson.toJson(returnObj);
    }
}


