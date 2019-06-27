package com.tokopedia.seller.product.draft.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.product.manage.item.main.add.di.ProductAddModule;
import com.tokopedia.product.manage.item.main.add.di.ProductAddScope;
import com.tokopedia.product.manage.item.main.draft.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.main.draft.data.source.ProductDraftDataSource;
import com.tokopedia.product.manage.item.main.draft.domain.DeleteSingleDraftProductUseCase;
import com.tokopedia.product.manage.item.main.draft.domain.ProductDraftRepository;
import com.tokopedia.product.manage.item.main.draft.domain.UpdateUploadingDraftProductUseCase;
import com.tokopedia.productdraftdatabase.ProductDraftDBCreation;
import com.tokopedia.productdraftdatabase.ProductDraftDao;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
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

    @ProductAddScope
    @Provides
    ProductDraftDao provideProductDraftDao(@ApplicationContext Context context){
        return ProductDraftDBCreation.getProductDraftDao(context);
    }

}

