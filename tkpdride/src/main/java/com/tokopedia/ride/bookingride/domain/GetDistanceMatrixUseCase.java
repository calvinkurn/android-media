package com.tokopedia.ride.bookingride.domain;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.ride.common.place.data.entity.DistanceMatrixEntity;
import com.tokopedia.ride.common.place.domain.PlaceRepository;

import rx.Observable;

/**
 * Created by alvarisi on 3/18/17.
 */

public class GetDistanceMatrixUseCase extends UseCase<DistanceMatrixEntity> {
    public static final String PARAM_ORIGINS = "origins";
    public static final String PARAM_DESTINATIONS = "destinations";
    public static final String PARAM_MODE = "mode";
    public static final String PARAM_KEY = "key";

    private final PlaceRepository placeRepository;

    public GetDistanceMatrixUseCase(ThreadExecutor threadExecutor,
                                    PostExecutionThread postExecutionThread,
                                    PlaceRepository placeRepository) {
        super(threadExecutor, postExecutionThread);
        this.placeRepository = placeRepository;
    }

    @Override
    public Observable<DistanceMatrixEntity> createObservable(RequestParams requestParams) {
        return this.placeRepository.getDistanceMatrix(requestParams.getParameters());
    }
}
