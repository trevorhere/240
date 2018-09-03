package model;
/**
 *  Event Model consisting of eventID , descendant, latitude, longitude,country, city, event type, year and person ID.
 *  <pre>
 *      <b>
 *          Domain:
 *      </b>
 *      eventID : String
 *      descendant : String
 *      latitude : Double
 *      longitude : Double
 *      country : String
 *      city : String
 *      eventType : String
 *      year: Int
 *      personID : String
 *  </pre>
 */

public class Event
{

   public String eventID;
   public String descendant;
   public String personID;
   public Double latitude;
   public Double longitude;
   public String country;
   public String city;
   public String eventType;
   public int year;

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }
    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }
    public void setPersonID(String person_ID) {
        this.personID = person_ID;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public void setCountry(String country) {
        this.country = country;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public void setYear(int year){this.year = year;
    }
}
