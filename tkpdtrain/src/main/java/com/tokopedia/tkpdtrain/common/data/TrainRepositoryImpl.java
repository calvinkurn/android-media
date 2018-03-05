package com.tokopedia.tkpdtrain.common.data;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.station.data.TrainStationDataStoreFactory;

/**
 * @author  by alvarisi on 3/5/18.
 */

public class TrainRepositoryImpl implements TrainRepository {
    private TrainDataStoreFactory dataStoreFactory;
    private TrainStationDataStoreFactory trainStationDataStoreFactory;

    public TrainRepositoryImpl(TrainDataStoreFactory dataStoreFactory, TrainStationDataStoreFactory trainStationDataStoreFactory){
        this.dataStoreFactory = dataStoreFactory;
        this.trainStationDataStoreFactory = trainStationDataStoreFactory;
    }


}
