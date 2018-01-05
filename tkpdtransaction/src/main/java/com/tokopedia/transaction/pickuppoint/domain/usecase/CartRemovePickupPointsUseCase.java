package com.tokopedia.transaction.pickuppoint.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.transaction.pickuppoint.data.repository.CartPickupPointRepository;
import com.tokopedia.transaction.pickuppoint.data.repository.PickupPointRepository;
import com.tokopedia.transaction.pickuppoint.domain.model.PickupPointResponse;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 5/1/18.
 */

public class CartRemovePickupPointsUseCase extends UseCase<PickupPointResponse> {

    private final CartPickupPointRepository repository;

    @Inject
    public CartRemovePickupPointsUseCase(ThreadExecutor threadExecutor,
                                         PostExecutionThread postExecutionThread,
                                         CartPickupPointRepository repository) {
        super(threadExecutor, postExecutionThread);
        this.repository = repository;
    }

    @Override
    public Observable<PickupPointResponse> createObservable(RequestParams requestParams) {
        return null;
    }

}
