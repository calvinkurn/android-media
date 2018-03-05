package com.tokopedia.tkpdtrain.common.domain;

import android.database.Observable;

import com.tokopedia.tkpdtrain.station.data.entity.TrainStationEntity;

import java.util.List;

/**
 * @author  by alvarisi on 3/5/18.
 */

public interface TrainRepository {
    Observable<List<TrainStationEntity>>
}
