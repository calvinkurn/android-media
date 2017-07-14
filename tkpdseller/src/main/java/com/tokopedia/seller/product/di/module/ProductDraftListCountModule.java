package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.di.module.ProductAddModule;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductCountUseCase;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenterImpl;

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
    ProductDraftListCountPresenter providePresenterDraft(FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase,
                                                         ClearAllDraftProductUseCase clearAllDraftProductUseCase,
                                                         UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase){
        return new ProductDraftListCountPresenterImpl(fetchAllDraftProductCountUseCase,
                clearAllDraftProductUseCase, updateUploadingDraftProductUseCase);
    }

    @ProductAddScope
    @Provides
    ClearAllDraftProductUseCase provideClearAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                                   ProductDraftRepository productDraftRepository){
        return new ClearAllDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }

    @ProductAddScope
    @Provides
    UpdateUploadingDraftProductUseCase provideUpdateUploadingDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                                   ProductDraftRepository productDraftRepository){
        return new UpdateUploadingDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }
}

