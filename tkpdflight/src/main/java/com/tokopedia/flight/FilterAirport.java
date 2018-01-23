package com.tokopedia.flight;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCity;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 11/20/17.
 */

public class FilterAirport {

    public String loadJSONFromAsset(Context context) throws IOException {
        String json = null;
        try {
            InputStream is = context.getAssets().open("flight_search_airport.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        Gson g = new Gson();
        Type dataResponseType = new TypeToken<DataResponse<List<FlightAirportCountry>>>() {}.getType();
        DataResponse<List<FlightAirportCountry>> dataResponse = g.fromJson(json, dataResponseType);

        DataResponse<List<FlightAirportCountry>> dataResponseOutput = new DataResponse<>();
        List<FlightAirportCountry> flightAirportCountries = new ArrayList<>();
        for(FlightAirportCountry flightAirportCountry : dataResponse.getData()){

            if(flightAirportCountry.getId().equals("ID")){
                flightAirportCountries.add(flightAirportCountry);
            }else{
                flightAirportCountry.getAttributes().setCities(new ArrayList<FlightAirportCity>());
                flightAirportCountries.add(flightAirportCountry);
            }
        }
        dataResponseOutput.setData(flightAirportCountries);

        File path = context.getExternalFilesDir(null);
        File file = new File(path, "jsonOutput.txt");
        FileOutputStream stream = new FileOutputStream(file);
        try {
            stream.write(g.toJson(dataResponseOutput).getBytes());
        } finally {
            stream.close();
        }
        return json;
    }
}
