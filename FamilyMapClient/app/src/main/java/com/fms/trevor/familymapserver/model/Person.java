package com.fms.trevor.familymapserver.model;

public class Person {
    private String descendant;
    private String personID;
    private String firstName;
    private String lastName;
    private String gender;
    private String father;
    private String mother;
    private String spouse;


    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }


    public Person(){}
    public Person(String descendant, String personID, String firstName, String lastName, String gender, String father, String  mother, String spouse ){
         this.setDescendant(descendant);
         this.setPersonID(personID);
         this.setFirstName(firstName);
         this.setLastName(lastName);
         this.setGender(gender);
         this.setFather(father);
         this.setMother(mother);
         this.setSpouse(spouse);
    }
}
