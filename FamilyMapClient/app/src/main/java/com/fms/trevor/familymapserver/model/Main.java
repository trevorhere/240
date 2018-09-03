package com.fms.trevor.familymapserver.model;

import android.app.Application;
import android.util.Log;
import android.widget.ExpandableListView;


import com.fms.trevor.familymapserver.HTTPPost;
import com.fms.trevor.familymapserver.MainActivity;
import com.fms.trevor.familymapserver.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;


public class Main extends Application {

    private static final String TAG = "Model.Main";
    public static String authToken = "";
    public static String personID = "";
    public static String currentUserPersonID = "";
    public static String userName = "";
    public static String url = "";
    public static String IP;
    public static String port;
    public static HashMap<String, Event> eventsMap;
    public static HashMap<String, Person> personsMap;
    public static ArrayList<String> eventTypes;
    public static Filter filters;


    public static void clear()
    {
        eventsMap.clear();
        personsMap.clear();
        eventTypes.clear();
        filters.filters.clear();

        currentUserPersonID = "";
        authToken = "";
        personID = "";
        url = "";

    }

    public static boolean reSync()
    {

       HashMap<String, Event> eventsTemp = (HashMap<String, Event> )eventsMap.clone();
       HashMap<String, Person> personsTemp = (HashMap<String, Person>) personsMap.clone();
       ArrayList<String> eventTypesTemp = ( ArrayList<String> )eventTypes.clone();
       Filter temp = filters;

        try
        {
            eventsMap.clear();
            personsMap.clear();
            eventTypes.clear();
            filters.filters.clear();

            fetchData();

            return true;

        }
        catch (Exception e)
        {
            eventsMap= (HashMap<String, Event> )eventsTemp.clone();
            personsMap = (HashMap<String, Person>) personsTemp .clone();
            eventTypes  = ( ArrayList<String> )eventTypesTemp .clone();
            filters = temp;
            return false;
        }

    }

    public static void fetchData()
    {
        fetchEvents();
        fetchPeople();
        fillFilter();

    }

    private static void fillFilter() {

        Set<String> filter = new HashSet<>();

        for(String eventType : eventTypes)
        {
            filter.add(eventType);
        }

        filters = new Filter(filter);
    }
    public boolean checkFilter(String fin) {

        boolean result = filters.filters.get(fin);
        return  result;
    }

    public static void filterEvents(String event, boolean filter) {
        Log.d(TAG, "filterEvents: " + event);


        if (filters.filters.containsKey(event))
        {
            if (event.equals("FATHERS"))
            {
                if(personsMap.get(currentUserPersonID).getFather() != null)
                {
                    recAncestor(personsMap.get(currentUserPersonID).getFather(), filter);

                }
            }
            else if (event.equals("MOTHERS"))
            {
                if(personsMap.get(currentUserPersonID).getMother() != null) {
                    recAncestor(personsMap.get(currentUserPersonID).getMother(), filter);
                }

            }
            else if (event.equals("MALE"))
            {
                filterMaleEvents(filter);
            }
            else if (event.equals("FEMALE"))
            {
                filterFemaleEvents(filter);
            }
            else {


                Event e = null;
                for (String key : eventsMap.keySet()) {
                    if (eventsMap.get(key).getEventType().toUpperCase().equals(event)) {
                        e = eventsMap.get(key);
                        e.setShowOnMap(filter);
                    }
                }
            }


                if (!filter) {
                    filters.filters.remove(event);
                    filters.filters.put(event, false);

                } else {
                    filters.filters.remove(event);
                    filters.filters.put(event, true);

                }

                MapFragment.reSync();





//
//        if(event.equals("FATHERS"))
//        {
//   //         filters.filters.  ("FATHERS")
//        }
//        else if(event.equals("MOTHERS"))
//        {
//
//        }
//        else if(event.equals("MALE"))
//        {
//
//        }
//        else if(event.equals("MOTHER"))
//        {
//
//        }
//        else
//        {
//                for(String key : filters.filters.keySet())
//                {
//                    if(eventsMap.get(key).getEventType().toUpperCase().equals(event))
//                    {
//                        if(!filter)
//                        {
//                            eventsMap.get(key).setShowOnMap(false);
//                        }
//                        else
//                        {
//                            eventsMap.get(key).setShowOnMap(true);
//
//                        }
//                }
//            }
//        }


        }
    }

    public static void recAncestor(String id, boolean filter)
    {

        if(id == null) return;
        Person p = personsMap.get(id);


        for(String key : eventsMap.keySet())
        {
            if(eventsMap.get(key).getPersonID().equals(id))
            {
                eventsMap.get(key).setShowOnMap(filter);
            }
        }

        if(p.getFather() != null)
        {
            recAncestor(p.getFather(), filter);
        }
        if(p.getMother() != null)
        {
            recAncestor(p.getMother(), filter);
        }
    }


    private static void filterMaleEvents(boolean filter)
    {
        for (String key : eventsMap.keySet())
        {

           if(personsMap.get(eventsMap.get(key).getPersonID()).getGender().equals("m"))
           {
                eventsMap.get(key).setShowOnMap(filter);
           }
        }

    }
    private static void filterFemaleEvents(boolean filter)
    {
        for (String key : eventsMap.keySet())
        {

            if(personsMap.get(eventsMap.get(key).getPersonID()).getGender().equals("f"))
            {
                eventsMap.get(key).setShowOnMap(filter);
            }
        }

    }

    public static String formatString(String str)
    {
        String fin = "";
        String[]  e = str.split(" ");
        for(int i = 0; i < e.length -1; i++)
        {
            fin += e[i];
            if(e.length > 2 && i != e.length + 2) {
                fin += " ";
            }
        }

        return fin;
    }
    public static void fetchEvents()
    {
        try
        {
            HashMap<String, String> postData = new HashMap<>();
            postData.put("route", "event");
            postData.put("authToken", authToken);
            JSONObject result = null;

            HTTPPost task = new HTTPPost(postData);
            try
            {
         //       result = task.execute("http://192.168.43.82:3000").get();
                result = task.execute(url ).get();
            }
            catch (Exception e)
            {
                Log.d(TAG, "fetchEvents: " + e.getMessage());
            }

            if(result != null)
            {
                JSONArray resultArray = result.getJSONArray("data");
                fillEvents(resultArray);
            }
        }
        catch (JSONException e)
        {
            Log.d(TAG, "fetchEvents: " + e.getMessage());
        }

    }
    public static void fillEvents(JSONArray events) {

        eventTypes = new ArrayList<>();
        eventsMap = new HashMap<>();
        try {

            for (int i = 0; i < events.length(); i++) {

                JSONObject eventObject = events.getJSONObject(i);
                Event event = new Event();
                event.setEventType(eventObject.getString("eventType"));
                event.setEventID(eventObject.getString("eventId"));
                event.setPersonID(eventObject.getString("personID"));
                event.setLat(eventObject.getString("latitude"));
                event.setLng(eventObject.getString("longitude"));
                event.setCountry(eventObject.getString("country"));
                event.setDescendant(eventObject.getString("descendant"));
                event.setCity(eventObject.getString("city"));
                event.setYear(eventObject.getString("year"));
                eventsMap.put(event.getEventID(), event);

                if(!eventTypes.contains(event.getEventType().toLowerCase()))
                {
                    eventTypes.add(event.getEventType().toLowerCase());
                }
            }
        }
        catch (JSONException e)
        {
            Log.d(TAG, "fillEvents: " + e.getMessage());
        }
    }

    public static void fetchPeople()
    {
        try
        {
            HashMap<String, String> postData = new HashMap<>();
            postData.put("route", "person");
            postData.put("authToken", authToken);
            JSONObject result = null;

            HTTPPost task = new HTTPPost(postData);
            try
            {
              //  result = task.execute("http://192.168.43.82:3000").get();
                result = task.execute(url ).get();

            }
            catch (Exception e)
            {
                Log.d(TAG, "fetchPeople: " + e.getMessage());
            }

            if(result != null)
            {
                JSONArray resultArray = result.getJSONArray("data");
                fillPeople(resultArray);
            }
        }
        catch (JSONException e)
        {
            Log.d(TAG, "fetchPeople: " + e.getMessage());
        }

    }
    public static void fillPeople(JSONArray people) {
        personsMap = new HashMap<>();
        try {

            for (int i = 0; i < people.length(); i++) {

                JSONObject personObject = people.getJSONObject(i);
                Person person = new Person();
                person.setDescendant(personObject.getString("descendant"));
                person.setPersonID(personObject.getString("personID"));
                person.setFirstName(personObject.getString("firstName"));
                person.setLastName(personObject.getString("lastName"));
                person.setGender(personObject.getString("gender"));

                if(personObject.has("mother"))
                {
                    person.setMother(personObject.getString("mother"));

                }
                if(personObject.has("father"))
                {
                    person.setFather(personObject.getString("father"));

                }

                if(personObject.has("spouse"))
                {
                    person.setSpouse(personObject.getString("spouse"));
                }
                personsMap.put(person.getPersonID(), person);
            }
        }
        catch (JSONException e)
        {
            Log.d(TAG, "fillPeople: " + e.getMessage());
        }
    }



    public static Event getEvent(String eventID)
    {
        if(eventsMap.containsKey(eventID))
        {
            return eventsMap.get(eventID);
        }

        return null;
    }

    public static Person getPerson(String personID)
    {
        if(personsMap.containsKey(personID))
        {
            return personsMap.get(personID);
        }

        return null;
    }

    public static ArrayList<Event> getEventsFromPersonID(String personID)
    {
        ArrayList<Event> eventsArray = new ArrayList<>();
        for(String key : eventsMap.keySet())
        {
            if(eventsMap.get(key).getPersonID().equals(personID))
            {
                eventsArray.add(eventsMap.get(key));
            }

        }

        return eventsArray;
    }

    public static void printEvents()
    {
        for(String key : eventsMap.keySet())
        {
            Log.e("EVENTID: " , key);
        }
    }


    public ArrayList<Person> getFamilyFromPersonID(String personID) {

        ArrayList<Person> familyArray = new ArrayList<>();
        Person currPerson = getPerson(personID);

        if(currPerson.getFather() != null && currPerson.getFather().length() > 0 )
        {
            familyArray.add(personsMap.get(currPerson.getFather()));
        }

        if(currPerson.getMother() != null && currPerson.getMother().length() > 0)
        {
            familyArray.add(personsMap.get(currPerson.getMother()));
        }

        if(currPerson.getSpouse() != null && currPerson.getSpouse().length() > 0)
        {
            familyArray.add(personsMap.get(currPerson.getSpouse()));
        }



        for(String key : personsMap.keySet())
        {
            Person p = personsMap.get(key);

            if(p.getMother() != null && p.getMother().equals(personID))
            {
                familyArray.add(personsMap.get(key));
            }
            if(p.getFather() != null &&  p.getFather().equals(personID))
            {
                familyArray.add(personsMap.get(key));
            }
        }

       return familyArray;
    }


}

