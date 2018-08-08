package com.tokopedia.seller.product.draft.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.product.manage.item.common.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.common.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.draft.domain.interactor.SaveBulkDraftProductUseCase;
import com.tokopedia.product.manage.item.common.domain.ProductDraftRepository;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenterImpl;
import com.tokopedia.product.manage.item.di.module.ProductAddModule;
import com.tokopedia.product.manage.item.di.scope.ProductAddScope;

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

