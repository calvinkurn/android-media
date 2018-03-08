package com.tokopedia.tkpdtrain.common.data;


import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.station.data.TrainStationDataStoreFactory;
import com.tokopedia.tkpdtrain.station.data.specification.TrainPopularStationSpecification;
import com.tokopedia.tkpdtrain.station.data.specification.TrainStationByKeywordSpecification;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;

import java.util.List;

import rx.Observable;

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

    @Override
    public Observable<List<TrainStation>> getPopularStations() {
        return trainStationDataStoreFactory.getStations(new TrainPopularStationSpecification());
    }

    @Override
    public Observable<List<TrainStation>> getStationsByKeyword(String keyword) {
        return trainStationDataStoreFactory.getStations(new TrainStationByKeywordSpecification(keyword));
    }
}
