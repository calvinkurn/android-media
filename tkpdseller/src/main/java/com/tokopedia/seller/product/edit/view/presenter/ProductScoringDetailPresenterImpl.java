package com.tokopedia.seller.product.edit.view.presenter;

import com.tokopedia.seller.product.edit.domain.interactor.ProductScoringUseCase;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.DataScoringProductView;
import com.tokopedia.seller.product.edit.view.model.scoringproduct.ValueIndicatorScoreModel;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 4/17/17.
 */

public class ProductScoringDetailPresenterImpl extends ProductScoringDetailPresenter {
    ProductScoringUseCase productScoringUseCase;

    public ProductScoringDetailPresenterImpl(ProductScoringUseCase productScoringUseCase) {
        this.productScoringUseCase = productScoringUseCase;
    }

    @Override
    public void getProductScoring(ValueIndicatorScoreModel valueIndicatorScoreModel) {
        getView().showProgress();
        productScoringUseCase.execute(ProductScoringUseCase.createRequestParams(valueIndicatorScoreModel),
                getSubscriberProductScoring());
    }

    public Subscriber<DataScoringProductView> getSubscriberProductScoring() {
        return new Subscriber<DataScoringProductView>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached()) {
                    return;
                }
                getView().dismissProgress();
                getView().onErrorGetScoringProduct();
            }

            @Override
            public void onNext(DataScoringProductView dataScoringProductView) {
                getView().dismissProgress();
                if(dataScoringProductView != null) {
                    getView().onSuccessGetScoringProduct(dataScoringProductView);
                }else{
                    getView().onErrorGetScoringProduct();
                }
            }
        };
    }
}
