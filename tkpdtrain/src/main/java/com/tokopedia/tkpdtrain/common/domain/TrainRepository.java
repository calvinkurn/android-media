package com.tokopedia.tkpdtrain.common.domain;


import com.tokopedia.tkpdtrain.station.data.entity.TrainStationEntity;
import com.tokopedia.tkpdtrain.station.domain.model.FlightStation;

import java.util.List;

import rx.Observable;

/**
 * @author  by alvarisi on 3/5/18.
 */

public interface TrainRepository {
    Observable<List<FlightStation>> getPopularStations();
}
