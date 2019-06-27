package com.tokopedia.seller.product.draft.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.product.manage.item.main.add.di.ProductAddModule;
import com.tokopedia.product.manage.item.main.add.di.ProductAddScope;
import com.tokopedia.product.manage.item.main.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.productdraftdatabase.ProductDraftDB;
import com.tokopedia.productdraftdatabase.ProductDraftDao;
import com.tokopedia.seller.product.draft.domain.interactor.SaveBulkDraftProductUseCase;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftSaveBulkPresenterImpl;

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

    @ProductAddScope
    @Provides
    ProductDraftDao provideProductDraftDao(@ApplicationContext Context context){
        return ProductDraftDB.getInstance(context).getProductDraftDao();
    }
}

