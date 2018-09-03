package test;


import database.Database;
import database.DatabaseException;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

public class TestRunner {
    public static void main(String[] args) {

        Result result = JUnitCore.runClasses(
                AuthTokenDaoTest.class,
                EventDaoTest.class,
                EventServiceTest.class,
                FillServiceTest.class,
                LoadServiceTest.class,
                LoginServiceTest.class,
                PersonDaoTest.class,
                RegisterServiceTest.class,
                UserDaoTest.class
                );


        Database db = new Database();
        db.start();
        try
        {
            db.clearDataBase();
            db.closeConnection(true);
        }
        catch (DatabaseException e)
        {
            e.getMessage();
        }


        for (Failure failure : result.getFailures()) {
            System.out.println(failure.toString());
        }

        if(result.wasSuccessful())
        {
            System.out.println("All tests passed");
        }
        
    }
}
