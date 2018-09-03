package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.User;
import request.LoginRequest;
import request.RegisterRequest;
import services.LoginService;
import services.RegisterService;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;

/**
 * This class handles the '/User' Route activities
 */

public class UserHandle implements HttpHandler {

    private Gson gson = new Gson();
    private String response = null;

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("User Handle");
        User user = new User();

        //List<String> RouteParams = getRoute(exchange.getRequestURI());
        String[] RouteParams = getRoute(exchange.getRequestURI());


        /** LOGIN */
        if(RouteParams[2].equals("login"))
        {
           try
           {
               InputStream requestBodyStream;
               requestBodyStream = exchange.getRequestBody();
               String requestBody = stringifyStream(requestBodyStream);
               // System.out.println("reqbody: " + requestBody);

               JsonObject json = gson.fromJson(requestBody, JsonObject.class);
               // System.out.println("json: " + json);
               // User user = new User();

               setUserForLogin(json, user);

               LoginRequest loginRequest = new LoginRequest(user);
               LoginService loginService = new LoginService();
               response = loginService.login(loginRequest);
           }
           catch (Exception e)
           {
               System.out.println("Error with /user/login Route: " + e.getMessage());
               response = messageObjToString("Internal server error");
           }
        }
        /** REGISTER */
        else if(RouteParams[2].equals("register"))
        {
            try
            {
                InputStream requestBodyStream;
                requestBodyStream = exchange.getRequestBody();
                String requestBody = stringifyStream(requestBodyStream);

                JsonObject json = gson.fromJson(requestBody, JsonObject.class);

                setUserForRegistration(json, user);
                RegisterRequest registerRequest = new RegisterRequest(user);
                RegisterService registerService = new RegisterService();

                if(!registerService.checkIfUsernameIsTaken(user.username))
                {

                    response = registerService.register(registerRequest);
                }
                else
                {
                    response = messageObjToString("Username already taken");
                }
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());

            }
        }
        else
        {
            response = messageObjToString("Issue with parameters");
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
    /**
     * This function gets user info from json and creates a User
     * @param json
     * @param user
     */
    public void setUserForLogin(JsonObject json, User user)
    {
        if(json != null  && json.has("userName"))
        {
            user.setUsername(json.get("userName").getAsString());
        }
        else
        {
            System.out.println("Issue with user name");
            return;
        }

        if(json != null && json.has("password"))
        {
            user.setPassword(json.get("password").getAsString());
        }
        else
        {
            System.out.println("Issue with password");
            return;
        }
    }

    /**
     * This function gets json info and creates a User
     * @param json
     * @param user
     */
    public void setUserForRegistration(JsonObject json, User user)
    {


        if(json != null  && json.has("userName"))
        {
           user.setUsername(json.get("userName").getAsString());
        }
        else
        {
            System.out.println("Issue with userName");
            return;
        }

        if(json != null && json.has("password"))
        {
            user.setPassword(json.get("password").getAsString());
        }
        else
        {
            System.out.println("Issue with password");
            return;
        }

        if(json != null  && json.has("email"))
        {
            user.setEmail(json.get("email").getAsString());
        }
        else
        {
            System.out.println("Issue with email");
            return;
        }

        if(json != null && json.has("firstName"))
        {
            user.setFirstName(json.get("firstName").getAsString());
        }
        else
        {
            System.out.println("Issue with firstName");
            return;
        }

        if(json != null  && json.has("lastName"))
        {
            user.setLastName(json.get("lastName").getAsString());
        }
        else
        {
            System.out.println("Issue with lastName");
            return;
        }

        if(json != null && json.has("gender"))
        {
            user.setGender(json.get("gender").getAsString());
        }
        else
        {
            System.out.println("Issue with gender");
            return;
        }

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
}
