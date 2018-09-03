package request;

public class EventRequest {
    public EventRequest(String authToken) {
        this.authToken = authToken;
    }
    public EventRequest(String authToken, String eventID) {
        this.authToken = authToken;
        this.eventID = eventID;
    }

    public String authToken;
    public String eventID;
}
