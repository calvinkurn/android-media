package com.tokopedia.flight.search.data.cloud;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.data.source.cloud.DataListCloudSource;
import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.search.data.cloud.model.FlightSearchData;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;

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
        // TODO use param for network
        // TODO this just dummy from asset
        String jsonString = loadJSONFromAsset();
        Type flightSearchType = new TypeToken<DataResponse<List<FlightSearchData>>>() {}.getType();
        DataResponse<List<FlightSearchData>> data = new Gson().fromJson(jsonString, flightSearchType);
        return Observable.just(data.getData());
//        return flightSearchApi.searchFlightSingle(new JsonObject()).flatMap(
//                new Func1<Response<DataResponse<List<FlightSearchData>>>,
//                        Observable<List<FlightSearchData>>>() {
//                    @Override
//                    public Observable<List<FlightSearchData>> call(Response<DataResponse<List<FlightSearchData>>> dataResponseResponse) {
//                        return Observable.just(dataResponseResponse.body().getData());
//                    }
//                });
    }


    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = BaseMainApplication.getAppContext().getAssets().open("flight_search_dummy.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

}