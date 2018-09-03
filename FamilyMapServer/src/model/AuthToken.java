package model;
/**
 *  AuthToken Model consisting of username, authToken, personID.
 *  <pre>
 *      <b>
 *          Domain:
 *      </b>
 *      username : String
 *      authToken: String
 *      personID: String
 *  </pre>
 */

public class AuthToken
{
    /** String username is the username value */
    public String username;
    /** String authToken is the authToken value */
    public String authToken;
    /** String personID is the personID value */
    public String personID;


    /**
     * Default Constructor for authToken
     */
    public AuthToken(){}

    /**
     * This is te class Constructor for the authToken
     * @param username This is the username
     * @param authToken This is the authToken
     * @param personID This is the personID
     */
    public AuthToken(String username, String authToken, String personID)
    {
        this.username = username;
        this.authToken = authToken;
        this.personID = personID;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public void setPersonID(String personID) {
        this.personID = personID;
    }


}
