package com.tokopedia.flight.search.data.cloud;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.common.data.model.request.DataRequest;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.search.data.cloud.model.request.FlightSearchSingleRequestData;
import com.tokopedia.flight.search.data.cloud.model.response.FlightDataResponse;
import com.tokopedia.flight.search.data.cloud.model.response.FlightSearchData;
import com.tokopedia.flight.search.util.FlightSearchParamUtil;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by normansyahputa on 5/18/17.
 */

public class FlightSearchDataListCloudSource extends DataListCloudSource<FlightSearchData> {

    private FlightApi flightApi;

    @Inject
    public FlightSearchDataListCloudSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    @Override
    public Observable<List<FlightSearchData>> getData(HashMap<String, Object> params) {
        boolean isReturning = FlightSearchParamUtil.isReturning(params);
        FlightSearchPassDataViewModel flightSearchPassDataViewModel = FlightSearchParamUtil.getInitialPassData(params);

        FlightSearchSingleRequestData flightSearchSingleRequestData =
                new FlightSearchSingleRequestData(flightSearchPassDataViewModel, isReturning);
        DataRequest<FlightSearchSingleRequestData> dataRequest = new DataRequest<>(flightSearchSingleRequestData);

        return flightApi.searchFlightSingle(dataRequest).flatMap(
                new Func1<Response<FlightDataResponse<List<FlightSearchData>>>,
                                        Observable<List<FlightSearchData>>>() {
                    @Override
                    public Observable<List<FlightSearchData>> call(Response<FlightDataResponse<List<FlightSearchData>>> dataResponseResponse) {
                        return Observable.just(dataResponseResponse.body().getData());
                    }
                });
    }

}