package request;

import model.AuthToken;
import model.User;

/**
 * Login Request
 * recieves a username and password,
 * returns success or error response
 *
 */
public class LoginRequest {


    public LoginRequest(User user)
    {
        this.user = user;
    }


    User user;
    AuthToken authToken;
    String userName;
    String personID;
    String password;
    String Message;


    /**
     * Login Result
     * Returns authToken, username and password if successful
     * Returns a message if error
     *
     */

    public void LoginResult(){

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    public void setAuthToken(AuthToken authToken) {
        this.authToken = authToken;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public AuthToken getAuthToken() {
        return authToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return Message;
    }
}


//exchagne to request (parse) -. login request...create service instance..login service opens connection to dao calls func in doa, then closes,
//  get json from db goes to model gson...login service is returend to handler ..echange has res body, write login res to response body ..