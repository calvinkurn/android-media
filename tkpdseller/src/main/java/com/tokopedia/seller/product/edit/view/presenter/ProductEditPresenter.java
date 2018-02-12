package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.edit.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.FetchEditProductWithVariantUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.core.common.category.domain.interactor.FetchCategoryDisplayUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.mapper.UploadProductMapper;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.variant.domain.interactor.FetchProductVariantByCatUseCase;

import javax.inject.Inject;

import rx.Subscriber;
/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditPresenter extends ProductAddPresenterImpl<ProductEditView> {
    private final FetchEditProductWithVariantUseCase fetchEditProductWithVariantUseCase;

    @Inject
    public ProductEditPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                GetCategoryRecommUseCase getCategoryRecommUseCase,
                                ProductScoringUseCase productScoringUseCase,
                                AddProductShopInfoUseCase addProductShopInfoUseCase,
                                FetchEditProductWithVariantUseCase fetchEditProductWithVariantUseCase,
                                FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase,
                                FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase) {
        super(saveDraftProductUseCase,
                fetchCatalogDataUseCase,
                getCategoryRecommUseCase,
                productScoringUseCase,
                addProductShopInfoUseCase,
                fetchCategoryDisplayUseCase,
                fetchProductVariantByCatUseCase);
        this.fetchEditProductWithVariantUseCase = fetchEditProductWithVariantUseCase;
    }

    public void fetchEditProductData(String productId) {
        RequestParams editParams = FetchEditProductWithVariantUseCase.createParams(productId);
        fetchEditProductWithVariantUseCase.execute(editParams, getFetchEditProductFormSubscriber());
    }

    @Override
    public void detachView() {
        super.detachView();
        fetchEditProductWithVariantUseCase.unsubscribe();
    }

    private Subscriber<UploadProductInputDomainModel> getFetchEditProductFormSubscriber() {
        return new Subscriber<UploadProductInputDomainModel>() {
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
            public void onNext(UploadProductInputDomainModel editProductFormDomainModel) {
                //TODO will be mapped to view model
//                UploadProductInputViewModel model = UploadProductMapper.mapDomainToView(editProductFormDomainModel);
                getView().onSuccessLoadDraftProduct(new ProductViewModel());
            }
        };
    }

}
