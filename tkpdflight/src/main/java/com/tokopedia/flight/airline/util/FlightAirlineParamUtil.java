package com.tokopedia.flight.airline.util;

import com.tokopedia.flight.airport.data.source.cloud.FlightAirportDataListCloudSource;

import java.util.HashMap;

/**
 * Created by User on 10/30/2017.
 */

public class FlightAirlineParamUtil {
    public static final String ID = "id";
    public static HashMap<String, Object> generateMap(String id){
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put(ID, id);
        return hashMap;
    }

    public static String getId(HashMap<String, Object> hashMap){
        return (String) hashMap.get(ID);
    }
}
