package com.tokopedia.tkpdtrain.common.constant;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.tkpdtrain.station.data.entity.TrainStationIslandEntity;

import java.util.List;
import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * @author  by alvarisi on 2/19/18.
 */

public interface TrainApi {

    @GET("kereta-api/stations")
    Observable<DataResponse<List<TrainStationIslandEntity>>> stationsInIsland(@QueryMap Map<String, Object> params);
}
