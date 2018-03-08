package com.tokopedia.tkpdtrain.station.domain;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.station.domain.model.TrainStation;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import rx.Observable;

/**
 * @author by alvarisi on 3/8/18.
 */

public class GetStationsByKeywordUseCase extends UseCase<List<TrainStation>> {
    private static final String PARAM_KEYWORD = "PARAM_KEYWORD";
    private TrainRepository trainRepository;

    public GetStationsByKeywordUseCase(TrainRepository trainRepository) {
        this.trainRepository = trainRepository;
    }

    @Override
    public Observable<List<TrainStation>> createObservable(RequestParams requestParams) {
        return trainRepository.getStationsByKeyword(requestParams.getString(PARAM_KEYWORD, ""));
    }

    public RequestParams createRequest(String keyword) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_KEYWORD, keyword);
        return requestParams;
    }
}
