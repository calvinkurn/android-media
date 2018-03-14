package com.tokopedia.tkpdtrain.search.domain;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainSchedule;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class GetAvailabilityScheduleUseCase extends UseCase<List<TrainSchedule>> {

    private TrainRepository trainRepository;

    private String idTrain;

    public GetAvailabilityScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void setIdTrain(String idTrain) {
        this.idTrain = idTrain;
    }

    @Override
    public Observable<List<TrainSchedule>> createObservable(RequestParams requestParams) {
        return trainRepository.getAvailabilitySchedule(idTrain);
    }
}
