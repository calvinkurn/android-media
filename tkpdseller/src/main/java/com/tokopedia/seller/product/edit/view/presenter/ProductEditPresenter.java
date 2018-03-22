package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.edit.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryDisplayUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.variant.domain.interactor.FetchProductVariantByCatUseCase;

import javax.inject.Inject;

import rx.Subscriber;
/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditPresenter extends ProductAddPresenterImpl<ProductEditView> {
    private final GetProductDetailUseCase getProductDetailUseCase;

    @Inject
    public ProductEditPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                GetCategoryRecommUseCase getCategoryRecommUseCase,
                                ProductScoringUseCase productScoringUseCase,
                                AddProductShopInfoUseCase addProductShopInfoUseCase,
                                GetProductDetailUseCase getProductDetailUseCase,
                                FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase,
                                FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase) {
        super(saveDraftProductUseCase,
                fetchCatalogDataUseCase,
                getCategoryRecommUseCase,
                productScoringUseCase,
                addProductShopInfoUseCase,
                fetchCategoryDisplayUseCase,
                fetchProductVariantByCatUseCase);
        this.getProductDetailUseCase = getProductDetailUseCase;
    }

    public void fetchEditProductData(String productId) {
        getProductDetailUseCase.execute(GetProductDetailUseCase.createParams(productId),
                getFetchEditProductFormSubscriber());
    }

    @Override
    public void detachView() {
        super.detachView();
        getProductDetailUseCase.unsubscribe();
    }

    private Subscriber<ProductViewModel> getFetchEditProductFormSubscriber() {
        return new Subscriber<ProductViewModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorFetchEditProduct(e);
            }

            @Override
            public void onNext(ProductViewModel productViewModel) {
                getView().onSuccessLoadProduct(productViewModel);
            }
        };
    }

}
