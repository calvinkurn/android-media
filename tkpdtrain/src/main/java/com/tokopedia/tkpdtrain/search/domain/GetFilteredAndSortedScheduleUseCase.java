package com.tokopedia.tkpdtrain.search.domain;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.search.constant.TrainSortOption;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainSchedule;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * Created by Rizky on 14/03/18.
 */

public class GetFilteredAndSortedScheduleUseCase extends UseCase<List<TrainSchedule>> {

    private static final String PARAM_FILTER = "PARAM_FILTER";
    private static final String PARAM_SORT_OPTION_ID = "PARAM_SORT_OPTION_ID";

    private TrainRepository trainRepository;

    public GetFilteredAndSortedScheduleUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<TrainSchedule>> createObservable(RequestParams requestParams) {
        FilterParam filterParam = (FilterParam) requestParams.getObject(PARAM_FILTER);
        int sortOptionId = requestParams.getInt(PARAM_SORT_OPTION_ID, TrainSortOption.NO_PREFERENCE);
        return trainRepository.getFilteredAndSortedSchedule(filterParam, sortOptionId);
    }

    public RequestParams createRequestParam(FilterParam filterParam, int sortOptionId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putObject(PARAM_FILTER, filterParam);
        requestParams.putInt(PARAM_SORT_OPTION_ID, sortOptionId);
        return requestParams;
    }

}
