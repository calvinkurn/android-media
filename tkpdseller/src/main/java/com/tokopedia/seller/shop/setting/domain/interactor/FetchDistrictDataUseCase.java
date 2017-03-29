package com.tokopedia.seller.shop.setting.domain.interactor;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.setting.domain.DistrictLogisticDataRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class FetchDistrictDataUseCase extends UseCase<Boolean> {
    private final DistrictLogisticDataRepository districtLogisticDataRepository;

    @Inject
    public FetchDistrictDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, DistrictLogisticDataRepository districtLogisticDataRepository) {
        super(threadExecutor, postExecutionThread);
        this.districtLogisticDataRepository = districtLogisticDataRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return districtLogisticDataRepository.fetchDistrictData();
    }
}
