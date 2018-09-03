package response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

/**
 * Login Response
 * Returns one of two possible responses
 * If login is successful an authorization token, username  and password are returned.
 * If login fails an error message is returned.
 */
public class LoginResponse {
    public LoginResponse( String token, String username, String personID ){
        this.token = token;
        this.username = username;
        this.personID = personID;
    }
    public LoginResponse(String message)
    {
        this.message = message;
    }
    public LoginResponse(){}

    String token = null;
    String message = null;
    String personID = null;
    String username = null;
    Gson gson = new Gson();
    JsonObject responseObj = new JsonObject();



    public String successResponse(String token, String username, String personID )
    {
        responseObj.addProperty("authToken", token);
        responseObj.addProperty("userName",  username);
        responseObj.addProperty("personId", personID);
        return gson.toJson(responseObj);
    }

    public String errorResponse(String message){

        responseObj.addProperty("Message", message);
        return gson.toJson(responseObj);
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
