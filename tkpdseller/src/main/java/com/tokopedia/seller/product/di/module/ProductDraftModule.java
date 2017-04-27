package com.tokopedia.seller.product.di.module;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.di.scope.ProductAddScope;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchDraftProductUseCase;
import com.tokopedia.seller.product.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.view.presenter.ProductDraftPresenter;

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
    ProductDraftPresenter provideProductDraftPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                                       FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                                       GetCategoryRecommUseCase getCategoryRecommUseCase,
                                                       ProductScoringUseCase productScoringUseCase,
                                                       AddProductShopInfoUseCase addProductShopInfoUseCase,
                                                       FetchDraftProductUseCase fetchDraftProductUseCase){
        return new ProductDraftPresenter(saveDraftProductUseCase, fetchCatalogDataUseCase, getCategoryRecommUseCase,
                productScoringUseCase, addProductShopInfoUseCase, fetchDraftProductUseCase);
    }

    @ProductAddScope
    @Provides
    FetchDraftProductUseCase provideFetchDraftProductUseCase(ThreadExecutor threadExecutor,
                                                             PostExecutionThread postExecutionThread,
                                                             ProductDraftRepository productDraftRepository){
        return new FetchDraftProductUseCase(threadExecutor, postExecutionThread, productDraftRepository);
    }
}
