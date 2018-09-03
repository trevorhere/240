package test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import database.Database;
import database.DatabaseException;
import org.junit.Test;
import request.LoadRequest;
import services.LoadService;

import java.io.FileNotFoundException;
import java.io.FileReader;

import static junit.framework.TestCase.assertEquals;

public class LoadServiceTest {
    Database db = new Database();

    @Test
    public void load()
    {

        db.start();
        try
        {
            db.openConnection();
            db.clearDataBase();
            db.closeConnection(true);
        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }

        LoadService ls = new LoadService();
        LoadRequest lr = new LoadRequest(importJSON());
        assertEquals(ls.load(lr), "{\"Message\":\"Successfully added 1 users, 3 persons, 2 events to the database\"}");



    }
    public JsonObject importJSON()
    {
        String fileName = "./json/example.json" ;
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = null;
        try
        {
            Object obj = parser.parse(new FileReader(fileName));
             jsonObject = (JsonObject) obj;

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return jsonObject ;
    }

}