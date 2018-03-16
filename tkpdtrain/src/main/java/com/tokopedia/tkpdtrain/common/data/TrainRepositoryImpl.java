package com.tokopedia.tkpdtrain.common.data;


import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.search.data.TrainScheduleDataStoreFactory;
import com.tokopedia.tkpdtrain.search.data.TrainScheduleSpecification;
import com.tokopedia.tkpdtrain.search.domain.FilterParam;
import com.tokopedia.tkpdtrain.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.tkpdtrain.station.data.TrainStationDataStoreFactory;
import com.tokopedia.tkpdtrain.station.data.specification.TrainPopularStationSpecification;
import com.tokopedia.tkpdtrain.station.data.specification.TrainStationByKeywordSpecification;
import com.tokopedia.tkpdtrain.station.data.specification.TrainStationCityByKeywordSpecification;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * @author by alvarisi on 3/5/18.
 */

public class TrainRepositoryImpl implements TrainRepository {

    private TrainDataStoreFactory dataStoreFactory;
    private TrainStationDataStoreFactory trainStationDataStoreFactory;
    private TrainScheduleDataStoreFactory trainScheduleDataStoreFactory;

    public TrainRepositoryImpl(TrainDataStoreFactory dataStoreFactory,
                               TrainStationDataStoreFactory trainStationDataStoreFactory,
                               TrainScheduleDataStoreFactory scheduleDataStoreFactory) {
        this.dataStoreFactory = dataStoreFactory;
        this.trainStationDataStoreFactory = trainStationDataStoreFactory;
        this.trainScheduleDataStoreFactory = scheduleDataStoreFactory;
    }

    @Override
    public Observable<List<TrainStation>> getPopularStations() {
        return trainStationDataStoreFactory.getStations(new TrainPopularStationSpecification());
    }

    @Override
    public Observable<List<TrainStation>> getStationsByKeyword(String keyword) {
        return trainStationDataStoreFactory.getStations(new TrainStationByKeywordSpecification(keyword));
    }

    @Override
    public Observable<List<AvailabilityKeySchedule>> getSchedule(Map<String, Object> mapParam) {
        return trainScheduleDataStoreFactory.getScheduleTrain(new TrainScheduleSpecification(mapParam));
    }

    @Override
    public Observable<List<TrainStation>> getStationCitiesByKeyword(String keyword) {
        return trainStationDataStoreFactory.getStations(new TrainStationCityByKeywordSpecification(keyword));
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> getAvailabilitySchedule(String idTrain) {
        return trainScheduleDataStoreFactory.getAvailabilitySchedule(idTrain);
    }

    @Override
    public Observable<List<TrainSchedule>> getFilteredAndSortedSchedule(FilterParam filterParam, int sortOptionId) {
        return trainScheduleDataStoreFactory.getFilteredAndSortedSchedule(filterParam, sortOptionId);
    }
}
