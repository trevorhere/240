package model;

/**
 *  User Model consisting of user name, password, email, first name, last name, gender, person_id.
 *  <pre>
 *      <b>
 *          Domain:
 *      </b>
 *      username : String
 *      password : String
 *      email : String
 *      first name: String
 *      last name : String
 *      gender : String
 *      personID : String
 *  </pre>
 */
public class User
{
    public String username;
    public String password;
    public String email;
    public String firstName;
    public String lastName;
    public String gender;
    public String personID;
    public String authToken;
    public User(User user) {
        this.username =  user.username;
        this.password =  user.password;
        this.email =     user.email;
        this.firstName = user.firstName;
        this.lastName =  user.lastName;
        this.gender =    user.gender;
        this.personID =  user.personID;
        this.authToken = user.authToken;
    }
    public User(){}
    public static User getTestUser() {
        User user = new User();
        user.username = "username";
        user.password = "password";
        user.personID = "personID";
        user.firstName = "firsname";
        user.lastName = "lastname";
        user.authToken = "authtoken";
        user.gender = "gender";
        user.email = "email";

        return user;
    }
    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) { this.password = password; }
    public void setEmail(String email) { this.email = email; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) {this.lastName = lastName; }
    public void setGender(String gender) {this.gender = gender; }
    public void setPersonID(String personID) { this.personID = personID; }
}
