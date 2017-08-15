package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.edit.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.FetchEditProductFormUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.category.domain.interactor.FetchCategoryDisplayUseCase;
import com.tokopedia.seller.product.draft.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.mapper.UploadProductMapper;
import com.tokopedia.seller.product.edit.view.model.upload.UploadProductInputViewModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;
import com.tokopedia.seller.product.variant.domain.interactor.FetchProductVariantByCatUseCase;
import com.tokopedia.seller.product.variant.domain.interactor.FetchProductVariantByPrdUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditPresenter extends ProductAddPresenterImpl<ProductEditView> {

    private final FetchEditProductFormUseCase fetchEditProductFormUseCase;
    private final FetchProductVariantByPrdUseCase fetchProductVariantByPrdUseCase;

    @Inject
    public ProductEditPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                GetCategoryRecommUseCase getCategoryRecommUseCase,
                                ProductScoringUseCase productScoringUseCase,
                                AddProductShopInfoUseCase addProductShopInfoUseCase,
                                FetchEditProductFormUseCase fetchEditProductFormUseCase,
                                FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase,
                                FetchProductVariantByCatUseCase fetchProductVariantByCatUseCase,
                                FetchProductVariantByPrdUseCase fetchProductVariantByPrdUseCase) {
        super(saveDraftProductUseCase,
                fetchCatalogDataUseCase,
                getCategoryRecommUseCase,
                productScoringUseCase,
                addProductShopInfoUseCase,
                fetchCategoryDisplayUseCase,
                fetchProductVariantByCatUseCase);
        this.fetchEditProductFormUseCase = fetchEditProductFormUseCase;
        this.fetchProductVariantByPrdUseCase = fetchProductVariantByPrdUseCase;
    }

    public void fetchEditProductData(String productId) {
        RequestParams params = FetchEditProductFormUseCase.createParams(productId);
        fetchEditProductFormUseCase.execute(params, getFetchEditProductFormSubscriber());
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
                UploadProductInputViewModel model = UploadProductMapper.mapDomainToView(editProductFormDomainModel);
                getView().onSuccessLoadDraftProduct(model);
            }
        };
    }

    public void fetchProductVariantByPrd(String productId) {
        RequestParams params = FetchProductVariantByPrdUseCase.generateParam(Long.parseLong(productId));
        fetchProductVariantByPrdUseCase.execute(params, getFetchProductVariantbyPrdSubscriber());
    }

    private Subscriber<ProductVariantByPrdModel> getFetchProductVariantbyPrdSubscriber() {
        return new Subscriber<ProductVariantByPrdModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().onErrorFetchProductVariantByPrd(e);
            }

            @Override
            public void onNext(ProductVariantByPrdModel productVariantByPrdModel) {
                getView().onSuccessFetchProductVariantByPrd(productVariantByPrdModel);
            }
        };
    }

}
