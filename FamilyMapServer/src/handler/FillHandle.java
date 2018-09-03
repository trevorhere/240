package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.FillRequest;
import services.FillService;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;

/** This class handles all '/Fill...' Route activity  */
public class FillHandle implements HttpHandler {


    private String response = null;
    Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        System.out.println("Fill Handle");
        String[] RouteParams = getRoute(exchange.getRequestURI());


        if (RouteParams.length == 3 || RouteParams.length == 4)
        {

                if (RouteParams.length == 3)
                {
                    String username = RouteParams[2];
                    FillRequest fillRequest = new FillRequest(username);
                    FillService fillService = new FillService();
                    response = fillService.fillFromUserName(fillRequest);
                }
                else
                {
                    Integer generations = Integer.parseInt(RouteParams[3]);

                    if(generations > 20)
                    {
                        response = messageObjToString("You do not need to generate that many ancestors");
                    }
                    else
                    {
                        String username = RouteParams[2];
                        FillRequest fillRequest = new FillRequest(username,generations);
                        FillService fillService = new FillService();
                        response = fillService.fillFromUserNameAddGenerations(fillRequest);
                    }

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
