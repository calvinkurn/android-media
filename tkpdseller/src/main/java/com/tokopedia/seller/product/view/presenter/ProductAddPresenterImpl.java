package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.AddProductUseCase;
import com.tokopedia.seller.product.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.mapper.UploadProductMapper;
import com.tokopedia.seller.product.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.view.model.scoringproduct.ValueIndicatorScoreModel;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductAddPresenterImpl extends ProductAddPresenter {
    private final SaveDraftProductUseCase saveDraftProductUseCase;
    private final ProductScoringUseCase productScoringUseCase;
    private final AddProductUseCase addProductUseCase;

    public ProductAddPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase, ProductScoringUseCase productScoringUseCase,
                                   AddProductUseCase addProductUseCase) {
        this.saveDraftProductUseCase = saveDraftProductUseCase;
        this.productScoringUseCase = productScoringUseCase;
        this.addProductUseCase = addProductUseCase;
    }

    @Override
    public void saveDraft(UploadProductInputViewModel viewModel) {
        UploadProductInputDomainModel domainModel = UploadProductMapper.map(viewModel);
        RequestParams requestParam = SaveDraftProductUseCase.generateUploadProductParam(domainModel);
        saveDraftProductUseCase.execute(requestParam, new SaveDraftSubscriber());
    }

    @Override
    public void getProductScoring(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        RequestParams requestParams = ProductScoringUseCase.createRequestParams(valueIndicatorScoreModel);
        productScoringUseCase.execute(requestParams, getSubscriberProductScoring());
    }

    public Subscriber<DataScoringProductView> getSubscriberProductScoring() {
        return new Subscriber<DataScoringProductView>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                checkViewAttached();
            }

            @Override
            public void onNext(DataScoringProductView dataScoringProductView) {
                checkViewAttached();
                if(dataScoringProductView != null) {
                    getView().onSuccessGetScoringProduct(dataScoringProductView);
                }
            }
        };
    }

    private class SaveDraftSubscriber extends Subscriber<Long> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
        }

        @Override
        public void onNext(Long productId) {
            RequestParams requestParam = AddProductUseCase.generateUploadProductParam(productId);
            addProductUseCase.execute(requestParam, new AddProductSubscriber());
        }
    }

    private class AddProductSubscriber extends Subscriber<AddProductDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            checkViewAttached();
        }

        @Override
        public void onNext(AddProductDomainModel addProductDomainModel) {
            checkViewAttached();
        }
    }
}
