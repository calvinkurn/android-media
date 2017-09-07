package com.tokopedia.seller.goldmerchant.featured.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.goldmerchant.featured.domain.model.FeaturedProductDomainModel;
import com.tokopedia.seller.goldmerchant.featured.repository.FeaturedProductRepository;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductUseCase extends UseCase<FeaturedProductDomainModel> {

    private FeaturedProductRepository featuredProductRepository;

    public FeaturedProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, FeaturedProductRepository featuredProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.featuredProductRepository = featuredProductRepository;
    }

    @Override
    public Observable<FeaturedProductDomainModel> createObservable(RequestParams requestParams) {
        return null;
    }
}
