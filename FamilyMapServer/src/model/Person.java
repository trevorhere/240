package model;
/**
 *  Person Model consisting of father, mother, descendant, first name, last name, gender, and person ID.
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

public class Person
{

    public String father;
    public String mother;
    public String spouse;
    public String descendant;
    public String firstName;
    public String lastName;
    public String gender;
    public String personID;

    public void setFather(String father) {this.father = father; }
    public void setMother(String mother) { this.mother = mother; }
    public void setSpouse(String spouse) { this.spouse = spouse ; }
    public void setDescendant(String descendant) {this.descendant = descendant; }
    public void setFirstName(String firstName) {this.firstName = firstName; }
    public void setLastName(String lastName) {this.lastName = lastName; }
    public void setGender(String gender) {this.gender = gender; }
    public void setPersonID(String personID) {this.personID = personID; }

}
