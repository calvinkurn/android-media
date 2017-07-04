package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.di.module.ProductAddModule;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.draft.domain.interactor.DeleteSingleDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListPresenterImpl;

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
    ProductDraftListPresenter providePresenterDraft(FetchAllDraftProductUseCase fetchAllDraftProductUseCase,
                                                    DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase){
        return new ProductDraftListPresenterImpl(fetchAllDraftProductUseCase, deleteSingleDraftProductUseCase);
    }

    @ProductAddScope
    @Provides
    DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                                   ProductDraftRepository productDraftRepository){
        return new DeleteSingleDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }
}

