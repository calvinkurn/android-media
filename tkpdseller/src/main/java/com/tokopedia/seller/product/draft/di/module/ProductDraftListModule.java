package com.tokopedia.seller.product.draft.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.product.manage.item.common.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.common.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.product.manage.item.di.module.ProductAddModule;
import com.tokopedia.product.manage.item.di.scope.ProductAddScope;
import com.tokopedia.product.manage.item.common.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.product.manage.item.common.domain.ProductDraftRepository;
import com.tokopedia.product.manage.item.common.domain.interactor.DeleteSingleDraftProductUseCase;
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
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context){
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @ProductAddScope
    @Provides
    ProductDraftListPresenter providePresenterDraft(FetchAllDraftProductUseCase fetchAllDraftProductUseCase,
                                                    DeleteSingleDraftProductUseCase deleteSingleDraftProductUseCase,
                                                    UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase,
                                                    ClearAllDraftProductUseCase clearAllDraftProductUseCase){
        return new ProductDraftListPresenterImpl(fetchAllDraftProductUseCase, deleteSingleDraftProductUseCase,
                updateUploadingDraftProductUseCase, clearAllDraftProductUseCase);
    }

}

