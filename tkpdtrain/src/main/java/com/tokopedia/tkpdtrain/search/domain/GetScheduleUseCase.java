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
    private static final String DATE_SCHEDULE = "date";
    private static final String TOTAL_ADULT = "adult";
    private static final String TOTAL_INFANT = "infant";
    private static final String ORIGIN_CODE = "origin";
    private static final String ORIGIN_CITY = "origin_city";
    private static final String DEST_CODE = "destination";
    private static final String DEST_CITY = "destination_city";

    private TrainRepository trainRepository;

    public GetScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<AvailabilityKeySchedule>> createObservable(RequestParams requestParams) {
        return trainRepository.getSchedule(requestParams.getParameters());
    }

    public RequestParams createRequest(String dateSchedule, int adult, int infant, String originCode,
                                       String originCity, String destCode, String destCity) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(DATE_SCHEDULE, dateSchedule);
        requestParams.putInt(TOTAL_ADULT, adult);
        requestParams.putInt(TOTAL_INFANT, infant);
        requestParams.putString(ORIGIN_CODE, originCode);
        requestParams.putString(ORIGIN_CITY, originCity);
        requestParams.putString(DEST_CODE, destCode);
        requestParams.putString(DEST_CITY, destCity);
        return requestParams;
    }
}
