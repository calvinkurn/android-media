package com.tokopedia.flight.dashboard.data.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author  by alvarisi on 10/30/17.
 */

public class FlightClassesDataSource {
    private FlightApi flightApi;

    @Inject
    public FlightClassesDataSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<List<FlightClassEntity>> getClasses() {
        return this.flightApi.getFlightClasses()
                .map(new Func1<Response<DataResponse<List<FlightClassEntity>>>, List<FlightClassEntity>>() {
                    @Override
                    public List<FlightClassEntity> call(Response<DataResponse<List<FlightClassEntity>>> response) {
                        return response.body().getData();
                    }
                });
    }
}
