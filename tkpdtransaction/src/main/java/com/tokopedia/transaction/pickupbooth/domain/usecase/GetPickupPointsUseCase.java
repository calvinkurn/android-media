package com.tokopedia.transaction.pickupbooth.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.transaction.pickupbooth.data.repository.PickupPointRepository;
import com.tokopedia.transaction.pickupbooth.domain.model.PickupPointResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class GetPickupPointsUseCase extends UseCase<PickupPointResponse> {
    public static final String PARAM_TOKEN = "token";
    public static final String PARAM_PAGE = "page";
    public static final String PARAM_UT = "ut";
    public static final String PARAM_QUERY = "query";
    public static final String PARAM_DISTRICT_ID = "district_id";

    public static final String DEFAULT_PAGE = "0";

    private final PickupPointRepository repository;

    @Inject
    public GetPickupPointsUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  PickupPointRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<PickupPointResponse> createObservable(RequestParams requestParams) {
        return repository.getPickupPoints(requestParams.getParamsAllValueInString());
    }

}
