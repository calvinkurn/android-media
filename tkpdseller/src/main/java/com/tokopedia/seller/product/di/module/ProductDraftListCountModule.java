package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.interactor.productdraft.FetchAllDraftProductCountUseCase;
import com.tokopedia.seller.product.domain.interactor.productdraft.FetchAllDraftProductUseCase;
import com.tokopedia.seller.product.view.presenter.ProductDraftListCountPresenter;
import com.tokopedia.seller.product.view.presenter.ProductDraftListCountPresenterImpl;
import com.tokopedia.seller.product.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.product.view.presenter.ProductDraftListPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User on 6/21/2017.
 */
@ProductAddScope
@Module
public class ProductDraftListCountModule extends ProductAddModule {

    @ProductAddScope
    @Provides
    FetchAllDraftProductCountUseCase provideFetchAllDraftProductCountUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                                         ProductDraftRepository productDraftRepository){
        return new FetchAllDraftProductCountUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }

    @ProductAddScope
    @Provides
    ProductDraftListCountPresenter providePresenterDraft(FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase){
        return new ProductDraftListCountPresenterImpl(fetchAllDraftProductCountUseCase);
    }
}

