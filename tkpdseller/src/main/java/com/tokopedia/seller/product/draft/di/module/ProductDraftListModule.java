package com.tokopedia.seller.product.draft.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.product.draft.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.draft.domain.interactor.SaveBulkDraftProductUseCase;
import com.tokopedia.seller.product.edit.di.module.ProductAddModule;
import com.tokopedia.seller.product.edit.di.scope.ProductAddScope;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
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
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource){
        return new ProductDraftRepositoryImpl(productDraftDataSource);
    }

    @ProductAddScope
    @Provides
    FetchAllDraftProductUseCase provideFetchAllDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                       ProductDraftRepository productDraftRepository){
        return new FetchAllDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }

    @ProductAddScope
    @Provides
    ProductDraftListPresenter providePresenterDraft(FetchAllDraftProductUseCase fetchAllDraftProductUseCase,
                                                    DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
                                                    UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase,
                                                    SaveBulkDraftProductUseCase saveBulkDraftProductUseCase){
        return new ProductDraftListPresenterImpl(fetchAllDraftProductUseCase, deleteSingleDraftProductUseCase,
                updateUploadingDraftProductUseCase, saveBulkDraftProductUseCase);
    }

    @ProductAddScope
    @Provides
    DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                                    ProductDraftRepository productDraftRepository){
        return new DeleteSingleDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }

    @ProductAddScope
    @Provides
    UpdateUploadingDraftProductUseCase provideUpdateUploadingDraftProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                                                    ProductDraftRepository productDraftRepository){
        return new UpdateUploadingDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }
}

