package handler;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class handles all '/' Route activities as well as invalid Route parameters
 */

public class Index implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {

        System.out.println("Index working");

        String filePathStr = "./web/index.html";
        Headers headers = exchange.getResponseHeaders();
        URI route_URI = exchange.getRequestURI();
        String route = route_URI.toString();

        String[] route_params = route.split("/", 2);

        if(route_params[1].equals("") || route_params.length <= 1 )
        {
            headers.set("Content-Type", "text/HTML");
        }
        else
        {
            String[] routeParameters = route.split("/");
            if( routeParameters[1].equals("css"))
            {
                headers.set("Content-Type", "text/css");
                filePathStr = "./web/css/main.css";

            }
            else if(routeParameters[1].equals("favicon.ico"))
            {
                headers.set("Content-Type", "image/png");
                filePathStr = "./web/favicon.jpg";
            }
            else
            {
                try
                {
                    exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK, 0);
                    OutputStreamWriter res = new OutputStreamWriter(exchange.getResponseBody(), Charset.forName("UTF-8"));
                    res.write(messageObjToString("Invalid route parameters"));
                    res.close();

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

        }


        Path filePath = FileSystems.getDefault().getPath(filePathStr);

        exchange.sendResponseHeaders(HttpURLConnection.HTTP_OK,0);

        try {

            Files.copy(filePath, exchange.getResponseBody());

        } catch( Exception e){
            System.out.println(e);
        }

        exchange.getResponseBody().close();

    }
    /**
     * This function coverts a string to a json object for response output
     * @param message
     * @return json response object
     */
    public String messageObjToString(String message){

        Gson gson = new Gson();
        JsonObject responseObj = new JsonObject();
        responseObj.addProperty("Message", message);
        return gson.toJson(responseObj);
    }
}
