package com.tokopedia.seller.opportunity.snapshot.presenter;

import android.content.Context;
import androidx.annotation.NonNull;

import com.tokopedia.core.product.facade.NetworkParam;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.seller.opportunity.domain.interactor.GetSnapShotProductUseCase;

import rx.Subscriber;

/**
 * Created by hangnadi on 3/1/17.
 */
public class SnapShotFragmentImpl extends SnapShotFragmentPresenter {
    private GetSnapShotProductUseCase useCase;

    public SnapShotFragmentImpl(GetSnapShotProductUseCase useCase) {
        this.useCase = useCase;
    }

    @Override
    public void processDataPass(ProductPass productPass) {
        if (productPass.haveBasicData()) getView().renderTempProductData(productPass);
    }

    @Override
    public void requestProductDetail(final String opportunityId, final ProductPass productPass,
                                     int type,
                                     boolean forceNetwork) {
        getView().showProgressLoading();
        getProductDetailFromNetwork(opportunityId, productPass);
    }

    private void getProductDetailFromNetwork(String opportunityId, ProductPass productPass) {

        useCase.execute(GetSnapShotProductUseCase.getRequestParams(productPass, opportunityId), new Subscriber<ProductDetailData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getView().showProductDetailRetry();
                getView().hideProgressLoading();
            }

            @Override
            public void onNext(ProductDetailData productDetailData) {
                getView().onProductDetailLoaded(productDetailData);
                getView().hideProgressLoading();
            }
        });
    }
}
