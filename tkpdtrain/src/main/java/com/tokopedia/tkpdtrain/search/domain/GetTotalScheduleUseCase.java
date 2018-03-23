package com.tokopedia.tkpdtrain.search.domain;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabillasabbaha on 3/21/18.
 */

public class GetTotalScheduleUseCase extends UseCase<Integer> {

    private static final String FILTER_PARAM = "filter_param";

    private TrainRepository trainRepository;

    public GetTotalScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<Integer> createObservable(RequestParams requestParams) {
        return trainRepository.getCountSchedule((FilterSearchData) requestParams.getObject(FILTER_PARAM));
    }

    public RequestParams createRequestParam(FilterSearchData filterSearchData) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(FILTER_PARAM, filterSearchData);
        return requestParams;
    }
}
