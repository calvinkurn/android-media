package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchEditProductFormUseCase;
import com.tokopedia.seller.product.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.domain.interactor.categorypicker.FetchCategoryDisplayUseCase;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.utils.ViewUtils;
import com.tokopedia.seller.product.view.mapper.UploadProductMapper;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class ProductEditPresenter extends ProductPopulatePresenter<ProductEditView> {

    private final FetchEditProductFormUseCase fetchEditProductFormUseCase;

    @Inject
    public ProductEditPresenter(SaveDraftProductUseCase saveDraftProductUseCase,
                                FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                GetCategoryRecommUseCase getCategoryRecommUseCase,
                                ProductScoringUseCase productScoringUseCase,
                                AddProductShopInfoUseCase addProductShopInfoUseCase,
                                FetchEditProductFormUseCase fetchEditProductFormUseCase,
                                FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase) {
        super(saveDraftProductUseCase,
                fetchCatalogDataUseCase,
                getCategoryRecommUseCase,
                productScoringUseCase,
                addProductShopInfoUseCase,
                fetchCategoryDisplayUseCase);
        this.fetchEditProductFormUseCase = fetchEditProductFormUseCase;
    }

    public void fetchEditProductData(String productId) {
        RequestParams params = FetchEditProductFormUseCase.createParams(productId);
        fetchEditProductFormUseCase.execute(params, new FetchEditProductFormSubscriber());
    }

    private class FetchEditProductFormSubscriber extends Subscriber<UploadProductInputDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            e.printStackTrace();
            checkViewAttached();
            getView().onErrorLoadProduct(ViewUtils.getErrorMessage(e));
        }

        @Override
        public void onNext(UploadProductInputDomainModel editProductFormDomainModel) {
            checkViewAttached();
            UploadProductInputViewModel model = UploadProductMapper.mapDomainToView(editProductFormDomainModel);
            getView().onSuccessLoadProduct(model);
        }
    }
}
