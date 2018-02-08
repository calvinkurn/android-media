package com.tokopedia.flight.search.data.cloud;

import com.tokopedia.abstraction.base.data.source.cloud.DataCloudSource;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.search.data.cloud.model.request.Attributes;
import com.tokopedia.flight.search.data.cloud.model.request.FlightSearchSingleRequestData;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.data.cloud.model.response.Meta;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchApiRequestModel;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;


public class FlightSearchDataCloudSource extends DataCloudSource<FlightDataResponse<List<FlightSearchData>>> {

    private FlightApi flightApi;

    @Inject
    public FlightSearchDataCloudSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    @Override
    public Observable<FlightDataResponse<List<FlightSearchData>>> getData(HashMap<String, Object> params) {
        FlightSearchApiRequestModel flightSearchApiRequestModel = FlightSearchParamUtil.getInitialPassData(params);

        FlightSearchSingleRequestData flightSearchSingleRequestData =
                new FlightSearchSingleRequestData(flightSearchApiRequestModel);
        DataRequest<FlightSearchSingleRequestData> dataRequest = new DataRequest<>(flightSearchSingleRequestData);

        return flightApi.searchFlightSingle(dataRequest).zipWith(Observable.just(flightSearchSingleRequestData),
                new Func2<Response<FlightDataResponse<List<FlightSearchData>>>,
                        FlightSearchSingleRequestData,
                        FlightDataResponse<List<FlightSearchData>>>() {
                    @Override
                    public FlightDataResponse<List<FlightSearchData>> call(Response<FlightDataResponse<List<FlightSearchData>>> flightDataResponseResponse,
                                                                           FlightSearchSingleRequestData flightSearchSingleRequestData) {
                        FlightDataResponse<List<FlightSearchData>> flightDataResponse = flightDataResponseResponse.body();
                        if (flightDataResponse != null) {
                            Meta meta = flightDataResponse.getMeta();
                            Attributes attribute = flightSearchSingleRequestData.getAttributes();
                            meta.setArrivalAirport(attribute.getArrival());
                            meta.setDepartureAirport(attribute.getDeparture());
                            meta.setTime(attribute.getDate());
                            return flightDataResponse;
                        } else {
                            return null;
                        }
                    }
                });
    }

}