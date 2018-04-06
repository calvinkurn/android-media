package com.tokopedia.seller.product.edit.di.module;

import com.tokopedia.seller.product.draft.domain.interactor.FetchDraftProductUseCase;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.di.scope.ProductAddScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author sebastianuskh on 4/26/17.
 */
@ProductAddScope
@Module
public class ProductDraftModule extends ProductAddModule {

    @ProductAddScope
    @Provides
    FetchDraftProductUseCase provideFetchDraftProductUseCase(ProductDraftRepository productDraftRepository) {
        return new FetchDraftProductUseCase(productDraftRepository);
    }

}
