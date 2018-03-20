package com.tokopedia.tkpdtrain.search.domain;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/12/18.
 */

public class GetAvailabilityScheduleUseCase extends UseCase<List<TrainScheduleViewModel>> {

    private TrainRepository trainRepository;

    private String idTrain;

    private int scheduleVariant;

    public GetAvailabilityScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void setIdTrain(String idTrain) {
        this.idTrain = idTrain;
    }

    public void setScheduleVariant(int scheduleVariant) {
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public Observable<List<TrainScheduleViewModel>> createObservable(RequestParams requestParams) {
        return trainRepository.getAvailabilitySchedule(idTrain, scheduleVariant);
    }
}
