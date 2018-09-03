package response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * FillResponse consists of an error response and success response
 * <pre>
 *     <b>Domain: </b>
 * message: String
 * </pre>
 */


public class FillResponse {
    Gson gson  = new Gson();
    JsonObject returnObj = new JsonObject();



    public String errorResponse(String message){
        returnObj.addProperty("Message", message);
        return gson.toJson(returnObj);
    }

    public String successResponse(String s)
    {
        returnObj.addProperty("Message", s);
        return gson.toJson(returnObj);
    }

    public class SuccessFillResponse
    {
        String message;
    }
}
