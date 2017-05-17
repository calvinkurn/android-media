package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.seller.product.domain.interactor.AddProductShopInfoUseCase;
import com.tokopedia.seller.product.domain.interactor.FetchCatalogDataUseCase;
import com.tokopedia.seller.product.domain.interactor.GetCategoryRecommUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.categorypicker.FetchCategoryDisplayUseCase;
import com.tokopedia.seller.product.domain.interactor.productdraft.FetchDraftProductUseCase;
import com.tokopedia.seller.product.domain.interactor.productdraft.SaveDraftProductUseCase;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.mapper.UploadProductMapper;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/26/17.
 */

public class ProductDraftPresenter extends ProductAddPresenterImpl<ProductDraftView>{

    private FetchDraftProductUseCase fetchDraftProductUseCase;

    @Inject
    public ProductDraftPresenter(SaveDraftProductUseCase saveDraftProductUseCase, FetchCatalogDataUseCase fetchCatalogDataUseCase,
                                 GetCategoryRecommUseCase getCategoryRecommUseCase, ProductScoringUseCase productScoringUseCase,
                                 AddProductShopInfoUseCase addProductShopInfoUseCase, FetchDraftProductUseCase fetchDraftProductUseCase,
                                 FetchCategoryDisplayUseCase fetchCategoryDisplayUseCase) {
        super(saveDraftProductUseCase, fetchCatalogDataUseCase, getCategoryRecommUseCase, productScoringUseCase, addProductShopInfoUseCase, fetchCategoryDisplayUseCase);
        this.fetchDraftProductUseCase = fetchDraftProductUseCase;
    }

    public void fetchDraftData(String draftId) {
        fetchDraftProductUseCase.execute(FetchDraftProductUseCase.createRequestParams(draftId), getSubsriberFetchDraft());
    }

    public Subscriber<UploadProductInputDomainModel> getSubsriberFetchDraft() {
        return new Subscriber<UploadProductInputDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                checkViewAttached();
                getView().onErrorLoadProduct(e);
            }

            @Override
            public void onNext(UploadProductInputDomainModel uploadProductInputDomainModel) {
                checkViewAttached();
                UploadProductInputViewModel model = UploadProductMapper.mapDomainToView(uploadProductInputDomainModel);
                getView().onSuccessLoadProduct(model);
            }
        };
    }
}
