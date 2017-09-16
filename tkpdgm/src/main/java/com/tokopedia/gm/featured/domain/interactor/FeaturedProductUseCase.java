package com.tokopedia.gm.featured.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.gm.featured.domain.model.FeaturedProductDomainModel;
import com.tokopedia.gm.featured.repository.FeaturedProductRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by normansyahputa on 9/6/17.
 */

public class FeaturedProductUseCase extends UseCase<FeaturedProductDomainModel> {

    private FeaturedProductRepository featuredProductRepository;

    @Inject
    public FeaturedProductUseCase(ThreadExecutor threadExecutor,
                                  PostExecutionThread postExecutionThread,
                                  FeaturedProductRepository featuredProductRepository) {
        super(threadExecutor, postExecutionThread);
        this.featuredProductRepository = featuredProductRepository;
    }

    @Override
    public Observable<FeaturedProductDomainModel> createObservable(RequestParams requestParams) {
        return featuredProductRepository.getFeatureProductData(requestParams);
    }
}
