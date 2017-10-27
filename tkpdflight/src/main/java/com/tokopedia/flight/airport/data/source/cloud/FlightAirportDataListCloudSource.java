package com.tokopedia.flight.airport.data.source.cloud;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.cloud.service.FlightAirportService;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightAirportDataListCloudSource extends DataListCloudSource<FlightAirportCountry> {

    public static final String KEYWORD = "keyword";

    private FlightAirportService flightAirportService;

    public FlightAirportDataListCloudSource(FlightAirportService flightAirportService) {
        super(flightAirportService);
        this.flightAirportService = flightAirportService;
    }

    @Override
    public Observable<List<FlightAirportCountry>> getData(HashMap<String, Object> params) {
        HashMap<String, String> paramsString = new HashMap<>();
        paramsString.put(KEYWORD, String.valueOf(params.get(KEYWORD)));
        return flightAirportService.getApi().getFlightAirportList(paramsString).flatMap(new Func1<Response<DataResponse<List<FlightAirportCountry>>>, Observable<List<FlightAirportCountry>>>() {
            @Override
            public Observable<List<FlightAirportCountry>> call(Response<DataResponse<List<FlightAirportCountry>>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }

}