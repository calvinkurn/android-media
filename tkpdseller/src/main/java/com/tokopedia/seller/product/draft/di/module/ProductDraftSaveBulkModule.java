package com.tokopedia.seller.product.draft.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.product.draft.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductCountUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.SaveBulkDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenterImpl;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenterImpl;
import com.tokopedia.seller.product.edit.di.module.ProductAddModule;
import com.tokopedia.seller.product.edit.di.scope.ProductAddScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User on 6/21/2017.
 */
@ProductAddScope
@Module
public class ProductDraftSaveBulkModule extends ProductAddModule {

    @ProductAddScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context){
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @ProductAddScope
    @Provides
    ProductDraftSaveBulkPresenter provideProductDraftSaveBulkPresenter(SaveBulkDraftProductUseCase saveBulkDraftProductUseCase){
        return new ProductDraftSaveBulkPresenterImpl(saveBulkDraftProductUseCase);
    }
}

