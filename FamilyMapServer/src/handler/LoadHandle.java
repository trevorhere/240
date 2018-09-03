package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import request.LoadRequest;
import services.LoadService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * This class handles all '/Load' Route activity
 */
public class LoadHandle implements HttpHandler {

    private String response = null;
    Gson gson = new Gson();


    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Load Handle");
        String[] RouteParams = getRoute(exchange.getRequestURI());


        if(RouteParams.length > 2 )
        {
            response = messageObjToString("Invalid parameters");
        }
        else
        {

            InputStream requestBodyStream;
            requestBodyStream = exchange.getRequestBody();
            String requestBody = stringifyStream(requestBodyStream);
            JsonObject json = gson.fromJson(requestBody, JsonObject.class);


            LoadRequest loadRequest = new LoadRequest(json);
            LoadService loadService = new LoadService();
            response = loadService.load(loadRequest);

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
    public String[] getRoute(URI route) {
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

    /**
     * This function gets converts and inputstream to a string
     * @param in Input Stream
     * @return String of data to process
     * @throws IOException
     */
    public static String stringifyStream(InputStream in) throws IOException
    {
        java.util.Scanner s = new java.util.Scanner(in).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}