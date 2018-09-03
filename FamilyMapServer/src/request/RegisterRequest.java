package request;

import model.User;

/**
 * RegisterRequest consisting of  username, passwrd, email, first name, last name, and gender.
 * <pre>
 *     <b>Domain: </b>
 *     authToken: String
 *     userName: String
 *     email: String
 *     firstName: String
 *     lastName: String
 *     gender: String
 *     message: String
 * </pre>
 */
public class RegisterRequest {
    public RegisterRequest(User user)
    {
        this.user = user;
    }



    User user;
    String authToken;
    String userName;
    String personID;
    String email;
    String firstName;
    String lastName;
    String gender;
    String message;

    /**
     * Register Result
     */
    public void RegisterResult(){ }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getMessage() {
        return message;
    }


    public String getUserName() {
        return userName;
    }

    public String getPersonID() {
        return personID;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }
}
