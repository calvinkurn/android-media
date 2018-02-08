package com.tokopedia.flight.airport.data.source.cloud;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.db.FlightAirportVersionDBSource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightAirportDataListFileSource extends DataListCloudSource<FlightAirportCountry> {

    public static final String KEYWORD = "keyword";
    public static final String FLIGHT_SEARCH_AIRPORT_JSON = "flight_search_airport.json";

    private Context context;
    private FlightAirportVersionDBSource flightAirportVersionDataSource;

    @Inject
    public FlightAirportDataListFileSource(@ApplicationContext Context context, FlightAirportVersionDBSource flightAirportVersionDataSource) {
        this.context = context;
        this.flightAirportVersionDataSource = flightAirportVersionDataSource;
    }

    @Override
    public Observable<List<FlightAirportCountry>> getData(HashMap<String, Object> params) {
        Gson g = new Gson();
        Type dataResponseType = new TypeToken<DataResponse<List<FlightAirportCountry>>>() {}.getType();
        DataResponse<List<FlightAirportCountry>> dataResponse = g.fromJson(loadJSONFromAsset(), dataResponseType);
        flightAirportVersionDataSource.setVersionDefault();
        return Observable.just(dataResponse.getData());
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = context.getAssets().open(FLIGHT_SEARCH_AIRPORT_JSON);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}