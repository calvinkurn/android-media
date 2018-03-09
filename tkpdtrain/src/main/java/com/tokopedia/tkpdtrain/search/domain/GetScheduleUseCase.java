package com.tokopedia.tkpdtrain.search.domain;

import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

/**
 * Created by nabilla on 3/9/18.
 */

public class GetScheduleUseCase extends UseCase<TrainSchedule> {

    @Override
    public Observable<TrainSchedule> createObservable(RequestParams requestParams) {
        return null;
    }
}
