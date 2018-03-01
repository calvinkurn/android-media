package com.tokopedia.flight.booking.data.cloud;

import com.tokopedia.flight.booking.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 22/02/18.
 */

public class FlightSavedPassengerDataListCloudSource {
    private FlightApi flightApi;

    @Inject
    public FlightSavedPassengerDataListCloudSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<List<SavedPassengerEntity>> getSavedPassenger() {
        return this.flightApi.getSavedPassengerData()
                .map(new Func1<Response<FlightDataResponse<List<SavedPassengerEntity>>>, List<SavedPassengerEntity>>() {
                    @Override
                    public List<SavedPassengerEntity> call(Response<FlightDataResponse<List<SavedPassengerEntity>>> flightDataResponseResponse) {
                        return flightDataResponseResponse.body().getData();
                    }
                });
    }
}
