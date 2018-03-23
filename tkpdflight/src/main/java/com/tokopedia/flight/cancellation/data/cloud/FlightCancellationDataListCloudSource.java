package com.tokopedia.flight.cancellation.data.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.cancellation.data.cloud.entity.CancelPassengerEntity;
import com.tokopedia.flight.cancellation.data.cloud.entity.Passenger;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;

import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by furqan on 23/03/18.
 */

public class FlightCancellationDataListCloudSource {

    private static final String PARAM_INVOICE_ID_KEY = "PARAM_INVOICE_ID_KEY";
    private FlightApi flightApi;

    @Inject
    public FlightCancellationDataListCloudSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<List<Passenger>> getData(String invoiceId) {
        return flightApi.getCancellablePassenger(invoiceId)
                .flatMap(new Func1<Response<DataResponse<CancelPassengerEntity>>, Observable<List<Passenger>>>() {
                    @Override
                    public Observable<List<Passenger>> call(Response<DataResponse<CancelPassengerEntity>> dataResponse) {
                        return Observable.just(dataResponse.body().getData()
                                .getAttributes().getPassengers());
                    }
                });
    }
}
