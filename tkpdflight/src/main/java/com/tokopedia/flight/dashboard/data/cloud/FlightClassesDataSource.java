package com.tokopedia.flight.dashboard.data.cloud;

import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.dashboard.data.cloud.entity.flightclass.FlightClassEntity;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author  by alvarisi on 10/30/17.
 */

public class FlightClassesDataSource {
    private FlightApi flightApi;

    public static final String TYPE = "class";

    @Inject
    public FlightClassesDataSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<List<FlightClassEntity>> getClasses() {
        List<FlightClassEntity> flightClassEntities = new ArrayList<>();
//        flightClassEntities.add(new FlightClassEntity(0, TYPE, "Semua"));
        flightClassEntities.add(new FlightClassEntity(1, TYPE, "Ekonomi"));
        flightClassEntities.add(new FlightClassEntity(2, TYPE, "Bisnis"));
        flightClassEntities.add(new FlightClassEntity(3, TYPE, "Utama"));
        return Observable.just(flightClassEntities);
        /*return this.flightApi.getFlightClasses()
                .map(new Func1<Response<DataResponse<List<FlightClassEntity>>>, List<FlightClassEntity>>() {
                    @Override
                    public List<FlightClassEntity> call(Response<DataResponse<List<FlightClassEntity>>> response) {
                        return response.body().getData();
                    }
                });*/
    }
}
