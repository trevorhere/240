package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import database.Database;
import database.DatabaseException;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.nio.charset.Charset;

/**
 * This class drops all database tables and recreates them
 */

public class ClearHandle implements HttpHandler {

    private String response = null;
    Gson gson  = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Clear working");

        Database db = new Database();
        try
        {
            db.start();
            db.clearDataBase();
            db.closeConnection(true);
            response =  messageObjToString("Clear succeeded");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            response = messageObjToString("Internal server error");
        }
        catch (DatabaseException e)
        {
            e.printStackTrace();
            response = messageObjToString("Internal server error");
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

