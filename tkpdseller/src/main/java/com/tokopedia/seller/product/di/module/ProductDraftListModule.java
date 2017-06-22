package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.ProductScoreRepository;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.productdraft.FetchAllDraftProductUseCase;
import com.tokopedia.seller.product.domain.interactor.productdraft.FetchDraftProductUseCase;
import com.tokopedia.seller.product.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.product.view.presenter.ProductDraftListPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User on 6/21/2017.
 */
@ProductAddScope
@Module
public class ProductDraftListModule extends ProductAddModule {

    @ProductAddScope
    @Provides
    FetchAllDraftProductUseCase provideFetchAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                       ProductDraftRepository productDraftRepository){
        return new FetchAllDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }

    @ProductAddScope
    @Provides
    ProductDraftListPresenter providePresenterDraft(FetchAllDraftProductUseCase fetchAllDraftProductUseCase){
        return new ProductDraftListPresenterImpl(fetchAllDraftProductUseCase);
    }
}

