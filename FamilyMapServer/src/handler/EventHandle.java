package handler;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.EventRequest;
import services.EventService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * This class handles all the '/event' Route
 */
public class EventHandle implements HttpHandler {

    Gson gson = new Gson();
    String response;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("EventHandler working");
        String[] RouteParams = getRoute(exchange.getRequestURI());


        if(RouteParams.length == 2 ||  RouteParams.length == 3)
        {

            String authToken = exchange.getRequestHeaders().getFirst("Authorization");
            if(authToken == null) response = messageObjToString("No Authorization token provide");

            EventRequest eventRequest = new EventRequest(authToken);
            EventService eventService = new EventService();

            if(eventService.getUserFromToken(authToken))
            {
                if(RouteParams.length == 2)
                {
                    response = eventService.getEventsFromUser(eventRequest);
                }
                else
                {
                    String eventID = RouteParams[2];
                    EventRequest er = new EventRequest(authToken,eventID);
                    response = eventService.getEventByID(er);
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