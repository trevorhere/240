package test;

import model.User;
import org.junit.Test;
import request.RegisterRequest;
import services.RegisterService;

import static junit.framework.TestCase.assertEquals;

public class RegisterServiceTest {

    @Test
    public void register()
    {
        RegisterService rs = new RegisterService();
        String[] response = rs.register(FormRegisterRequest()).split("\"");
        assertEquals(response[7],"test");

    }
    public RegisterRequest FormRegisterRequest()
    {
        User user  = new User();
        user.username =  "test";
        user.authToken = "test";
        user.lastName =  "test";
        user.firstName = "test";
        user.gender =    "test";
        user.lastName =  "test";
        user.email =     "test";
        user.password =  "test";
        user.personID =  "test";


        RegisterRequest rr = new RegisterRequest(user);
        return rr;

    }
}