package response;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Person;

import java.util.List;


public class PersonResponse {

    Gson gson  = new Gson();
    JsonArray personArray = new JsonArray();
    JsonObject returnObj = new JsonObject();



    public String fetchAllPeopleSuccessResponse(List<Person> personList)
    {

        for(Person p : personList)
        {
            JsonObject person = new JsonObject(); //dataset
            person.addProperty("personID", p.personID);
            person.addProperty("descendant", p.descendant);
            person.addProperty("firstName", p.firstName);
            person.addProperty("lastName", p.lastName);
            person.addProperty("gender", p.gender);
            person.addProperty("father", p.father);
            person.addProperty("mother", p.mother);
            person.addProperty("spouse", p.spouse);
            personArray.add(person);
        }

        returnObj.add("data", personArray);
        return gson.toJson(returnObj);

    }
    public String getPersonSuccessResponse(Person person)
    {

        returnObj.addProperty("descendant", person.descendant);
        returnObj.addProperty("personID",   person.personID);
        returnObj.addProperty("firsName",   person.firstName);
        returnObj.addProperty("lastName",   person.lastName);
        returnObj.addProperty("gender",     person.gender);
        returnObj.addProperty("father",     person.father);
        returnObj.addProperty("mother",     person.mother);
        returnObj.addProperty("spouse",     person.spouse);
        return gson.toJson(returnObj);
    }
    public String errorResponse(String message){

        returnObj.addProperty("Message", message);
        return gson.toJson(returnObj);
    }



}
