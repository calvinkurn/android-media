package com.tokopedia.tkpdtrain.station.domain;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.station.domain.model.FlightStation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author by alvarisi on 3/7/18.
 */

public class GetPopularStationsUseCase extends UseCase<List<FlightStation>> {
    private TrainRepository trainRepository;

    public GetPopularStationsUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<FlightStation>> createObservable(RequestParams requestParams) {
        return trainRepository.getPopularStations();
    }
}
