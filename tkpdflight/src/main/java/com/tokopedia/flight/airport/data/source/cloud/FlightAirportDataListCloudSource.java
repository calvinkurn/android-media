package com.tokopedia.flight.airport.data.source.cloud;

import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.airport.data.source.cloud.model.FlightAirportCountry;
import com.tokopedia.flight.airport.data.source.cloud.service.FlightAirportService;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightAirportDataListCloudSource extends DataListCloudSource<FlightAirportCountry> {

    private FlightAirportService flightAirportService;

    public FlightAirportDataListCloudSource(FlightAirportService flightAirportService) {
        super(flightAirportService);
        this.flightAirportService = flightAirportService;
    }

    @Override
    public Observable<List<FlightAirportCountry>> getData() {
        return flightAirportService.getApi().getFlightAirportList().flatMap(new Func1<Response<DataResponse<List<FlightAirportCountry>>>, Observable<List<FlightAirportCountry>>>() {
            @Override
            public Observable<List<FlightAirportCountry>> call(Response<DataResponse<List<FlightAirportCountry>>> dataResponseResponse) {
                return Observable.just(dataResponseResponse.body().getData());
            }
        });
    }
}