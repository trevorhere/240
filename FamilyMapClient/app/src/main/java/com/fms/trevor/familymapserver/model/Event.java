package com.fms.trevor.familymapserver.model;

import android.support.annotation.NonNull;

public class Event {

    private  String eventType;
    private String descendant;
    private String lat;
    private String lng;
    private String country;
    private String city;
    private String year;
    private String personID;
    private String eventID;


    private boolean showOnMap = true;

    public Event(){}

    public Event(String eventType, String descendant, String lat, String lng, String country, String city, String year, String personID)
    {
        this.setEventID(eventType);
        this.setDescendant(descendant);
        this.setLat(lat);
        this.setLng(lng);
        this.setCountry(country);
        this.setCity(city);
        this.setPersonID(personID);
        this.setYear(year);


    }



    public boolean isShowOnMap() {
        return showOnMap;
    }

    public void setShowOnMap(boolean showOnMap) {
        this.showOnMap = showOnMap;
    }


    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String description) {
        this.eventType= description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }






}
