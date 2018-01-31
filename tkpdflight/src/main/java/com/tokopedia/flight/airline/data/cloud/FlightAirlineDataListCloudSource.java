package com.tokopedia.flight.airline.data.cloud;

import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.airline.data.cloud.model.AirlineData;
import com.tokopedia.flight.airline.data.db.model.FlightAirlineDB;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.common.di.scope.FlightScope;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightAirlineDataListCloudSource extends DataListCloudSource<AirlineData> {

    public static final String ID = "id";

    private FlightApi flightApi;

    @Inject
    public FlightAirlineDataListCloudSource(@FlightScope FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    @Override
    public Observable<List<AirlineData>> getData(HashMap<String, Object> params) {
        return flightApi.getFlightAirlineList().flatMap(new Func1<Response<DataResponse<List<AirlineData>>>, Observable<List<AirlineData>>>() {
            @Override
            public Observable<List<AirlineData>> call(Response<DataResponse<List<AirlineData>>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }

    public Observable<AirlineData> getAirline(String airlineId) {
        return flightApi.getFlightAirline(airlineId).flatMap(new Func1<Response<DataResponse<List<AirlineData>>>, Observable<List<AirlineData>>>() {
            @Override
            public Observable<List<AirlineData>> call(Response<DataResponse<List<AirlineData>>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        }).flatMap(new Func1<List<AirlineData>, Observable<AirlineData>>() {
            @Override
            public Observable<AirlineData> call(List<AirlineData> airlineData) {
                if (airlineData != null && airlineData.size() > 0)
                    return Observable.just(airlineData.get(0));
                else
                    return Observable.empty();
            }
        });
    }
}