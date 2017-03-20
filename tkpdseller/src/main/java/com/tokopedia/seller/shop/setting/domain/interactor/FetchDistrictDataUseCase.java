package com.tokopedia.seller.shop.setting.domain.interactor;


import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.shop.setting.domain.DistrictDataRepository;

import rx.Observable;

/**
 * Created by sebastianuskh on 3/20/17.
 */

public class FetchDistrictDataUseCase extends UseCase<Boolean> {
    private final DistrictDataRepository districtDataRepository;

    public FetchDistrictDataUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, DistrictDataRepository districtDataRepository) {
        super(threadExecutor, postExecutionThread);
        this.districtDataRepository = districtDataRepository;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        return districtDataRepository.fetchDistrictData();
    }
}
