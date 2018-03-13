package com.tokopedia.tkpdtrain.station.domain;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author by alvarisi on 3/7/18.
 */

public class TrainGetPopularStationsUseCase extends UseCase<List<TrainStation>> {
    private TrainRepository trainRepository;

    public TrainGetPopularStationsUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<TrainStation>> createObservable(RequestParams requestParams) {
        return trainRepository.getPopularStations();
    }

    public RequestParams createRequest() {
        return RequestParams.create();
    }
}
