package com.tokopedia.seller.product.di.module;

import com.tokopedia.seller.product.data.repository.ProductDraftRepositoryImpl;
import com.tokopedia.seller.product.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.view.presenter.ProductAddPresenter;
import com.tokopedia.seller.product.view.presenter.ProductAddPresenterImpl;

import dagger.Module;
import dagger.Provides;

/**
 * @author sebastianuskh on 4/13/17.
 */
@ProductAddScope
@Module
public class ProductAddModule {

    @ProductAddScope
    @Provides
    ProductAddPresenter provideProductAddPresenter(SaveDraftProductUseCase saveDraftProductUseCase){
        return new ProductAddPresenterImpl(saveDraftProductUseCase);
    }

    @ProductAddScope
    @Provides
    ProductDraftRepository provideProductDraftRepository(ProductDraftDataSource productDraftDataSource){
        return new ProductDraftRepositoryImpl(productDraftDataSource);
    }

}
