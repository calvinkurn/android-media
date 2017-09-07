package com.tokopedia.seller.goldmerchant.featured.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductPOSTDomainModel;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductPOSTUseCase extends UseCase<FeaturedProductPOSTDomainModel> {
    public FeaturedProductPOSTUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    public Observable<FeaturedProductPOSTDomainModel> createObservable(RequestParams requestParams) {

        return null;
    }
}
