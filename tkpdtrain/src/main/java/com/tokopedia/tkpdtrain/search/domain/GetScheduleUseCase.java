package com.tokopedia.tkpdtrain.search.domain;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.search.presentation.model.AvailabilityKeySchedule;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by nabilla on 3/9/18.
 */

public class GetScheduleUseCase extends UseCase<List<AvailabilityKeySchedule>> {
    public static final String DATE_SCHEDULE = "date";
    public static final String TOTAL_ADULT = "adult";
    public static final String TOTAL_INFANT = "infant";
    public static final String ORIGIN_CODE = "origin";
    public static final String ORIGIN_CITY = "origin_city";
    public static final String DEST_CODE = "destination";
    public static final String DEST_CITY = "destination_city";

    private int scheduleVariant;

    private TrainRepository trainRepository;

    public GetScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    public void setScheduleVariant(int scheduleVariant) {
        this.scheduleVariant = scheduleVariant;
    }

    @Override
    public Observable<List<AvailabilityKeySchedule>> createObservable(RequestParams requestParams) {
        return trainRepository.getSchedule(requestParams.getParameters(), scheduleVariant);
    }
}
