package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.seller.product.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;

import javax.inject.Inject;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftPresenter extends ProductAddPresenterImpl<ProductDraftView>{

    @Inject
    public ProductDraftPresenter(SaveDraftProductUseCase saveDraftProductUseCase, FetchCatalogDataUseCase fetchCatalogDataUseCase, GetCategoryRecommUseCase getCategoryRecommUseCase, ProductScoringUseCase productScoringUseCase, AddProductShopInfoUseCase addProductShopInfoUseCase) {
        super(saveDraftProductUseCase, fetchCatalogDataUseCase, getCategoryRecommUseCase, productScoringUseCase, addProductShopInfoUseCase);
    }
}
