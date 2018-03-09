package com.tokopedia.tkpdtrain.common.domain;


import com.tokopedia.tkpdtrain.search.domain.TrainSchedule;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;
import com.tokopedia.usecase.RequestParams;

import java.util.List;

import rx.Observable;

/**
 * @author  by alvarisi on 3/5/18.
 */

public interface TrainRepository {
    Observable<List<TrainStation>> getPopularStations();

    Observable<List<TrainStation>> getStationsByKeyword(String keyword);

    Observable<List<TrainStation>> getStationCitiesByKeyword(String keyword);

    Observable<List<TrainSchedule>> getSchedule(RequestParams);
}
