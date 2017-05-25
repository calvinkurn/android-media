package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.interactor.productdraft.FetchDraftProductUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author sebastianuskh on 4/26/17.
 */
@ProductAddScope
@Module
public class ProductDraftModule extends ProductAddModule {

    @ProductAddScope
    @Provides
    FetchDraftProductUseCase provideFetchDraftProductUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ProductDraftRepository productDraftRepository){
        return new FetchDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }

}
