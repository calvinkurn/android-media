package com.tokopedia.seller.product.draft.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.product.manage.item.common.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.product.manage.item.common.data.source.ProductDraftDataSource;
import com.tokopedia.product.manage.item.common.domain.interactor.UpdateUploadingDraftProductUseCase;
import com.tokopedia.product.manage.item.common.domain.ProductDraftRepository;
import com.tokopedia.seller.product.draft.domain.interactor.ClearAllDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.FetchAllDraftProductCountUseCase;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenter;
import com.tokopedia.seller.product.draft.view.presenter.ProductDraftListCountPresenterImpl;
import com.tokopedia.seller.product.manage.di.ProductManageModule;
import com.tokopedia.seller.product.manage.di.ProductManageScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by User on 6/21/2017.
 */
@ProductManageScope
@Module(includes = ProductManageModule.class)
public class ProductDraftListCountModule{

    @ProductManageScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource, @ApplicationContext Context context){
        return new ProductDraftRepositoryImpl(productDraftDataSource, context);
    }

    @ProductManageScope
    @Provides
    ProductDraftListCountPresenter providePresenterDraft(FetchAllDraftProductCountUseCase fetchAllDraftProductCountUseCase,
                                                         ClearAllDraftProductUseCase clearAllDraftProductUseCase,
                                                         UpdateUploadingDraftProductUseCase updateUploadingDraftProductUseCase){
        return new ProductDraftListCountPresenterImpl(fetchAllDraftProductCountUseCase,
                clearAllDraftProductUseCase, updateUploadingDraftProductUseCase);
    }

}

