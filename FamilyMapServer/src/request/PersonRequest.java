package request;

public class PersonRequest {
    public PersonRequest(String authToken)
    {
        this.authToken = authToken;
    }
    public PersonRequest(String authToken, String personID)
    {
        this.authToken = authToken;
        this.personID = personID;
    }

    public String authToken;
    public String personID;

}
