package com.tokopedia.flight.booking.data.cloud;

import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.flight.booking.data.cloud.entity.SavedPassengerEntity;
import com.tokopedia.flight.booking.data.cloud.requestbody.DeletePassengerRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 22/02/18.
 */

public class FlightSavedPassengerDataListCloudSource extends DataListCloudSource<SavedPassengerEntity> {
    private FlightApi flightApi;

    @Inject
    public FlightSavedPassengerDataListCloudSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    @Override
    public Observable<List<SavedPassengerEntity>> getData(HashMap<String, Object> params) {
        return this.flightApi.getSavedPassengerData()
                .flatMap(new Func1<Response<FlightDataResponse<List<SavedPassengerEntity>>>, Observable<List<SavedPassengerEntity>>>() {
                    @Override
                    public Observable<List<SavedPassengerEntity>> call(Response<FlightDataResponse<List<SavedPassengerEntity>>> flightDataResponseResponse) {
                        return Observable.just(flightDataResponseResponse.body().getData());
                    }
                });
    }

    public Observable<Response<String>> deletePassenger(DeletePassengerRequest request, String idempotencyKey) {
        return this.flightApi.deleteSavedPassengerData(new DataRequest<>(request), idempotencyKey);
    }
}
