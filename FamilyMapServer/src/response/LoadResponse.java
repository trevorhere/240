package response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * LoadResponse
 * returns success or error message
 */
public class LoadResponse {

    Gson gson = new Gson();
    JsonObject responseObj = new JsonObject();

    public String successResponse(int usersLength, int personsLength, int eventsLength)
    {
        String message = "Successfully added " + usersLength + " users, " + personsLength + " persons, " + eventsLength + " events to the database";
        responseObj.addProperty("Message", message);
        return gson.toJson(responseObj);
    }

    public String errorResponse(String message)
    {
        responseObj.addProperty("Message", message);
        return gson.toJson(responseObj);

    }
}
