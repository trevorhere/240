package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.PersonRequest;
import services.PersonService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * This class handles all '/Person' Route activity
 */
public class PersonHandle implements HttpHandler {

    private String response = null;
    Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Person Handle");
        String[] RouteParams = getRoute(exchange.getRequestURI());

        if(RouteParams.length == 2 ||  RouteParams.length == 3)
        {
            String authToken = exchange.getRequestHeaders().getFirst("Authorization");
            if(authToken == null) response = messageObjToString("No Authorization token provide");

            PersonRequest personRequest= new PersonRequest(authToken);
            PersonService personService = new PersonService();


            if(personService.authenticateAuthToken(authToken))
            {
                if(RouteParams.length == 2)
                {
                    response = personService.getPeopleFromUser(personRequest);
                }
                else
                {
                    String personID = RouteParams[2];
                    PersonRequest pr = new PersonRequest(authToken, personID);
                    response = personService.getPersonByID(pr);
                }
            }
            else
            {
                response = messageObjToString("Invalid Authorization token");
            }
        }
        else
        {
            System.out.println("Invalid Parameters");
        }

        try
        {
            exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
            OutputStreamWriter res = new OutputStreamWriter(exchange.getResponseBody(), Charset.forName("UTF-8"));
            res.write(response);
            res.close();

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



    }
    /**
     * This function gets a String array of route parameters from URI
     * @param route
     * @return String array of route parameters
     */
    public String[] getRoute(URI route)
    {
        String result = route.toString();
        return result.split("/");
    }
    /**
     * This function coverts a string to a json object for response output
     * @param message
     * @return json response object
     */
    public String messageObjToString(String message){

        JsonObject responseObj = new JsonObject();
        responseObj.addProperty("Message", message);
        return gson.toJson(responseObj);
    }


}