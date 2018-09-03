package com.fms.trevor.familymapserver.model;

import java.util.HashMap;
import java.util.Set;

public class Filter {


    public static HashMap<String, Boolean> filters;


    public Filter(Set<String> eventTypes)
    {

        filters = new HashMap<String, Boolean>();
        for(String types : eventTypes)
        {
            types = types.toUpperCase(); //change to upper
            filters.put(types, true);
        }

        filters.put("MOTHERS", true);
        filters.put("FATHERS", true);
        filters.put("MALE", true);
        filters.put("FEMALE", true);

    }
}
