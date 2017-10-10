package com.tokopedia.seller.opportunity.snapshot.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.tokopedia.core.product.facade.NetworkParam;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractorImpl;
import com.tokopedia.core.product.model.productdetail.ProductDetailData;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.seller.opportunity.snapshot.listener.SnapShotFragmentView;

/**
 * Created by hangnadi on 3/1/17.
 */
public class SnapShotFragmentImpl implements SnapShotFragmentPresenter {
    private final SnapShotFragmentView viewListener;
    private RetrofitInteractor retrofitInteractor;

    public SnapShotFragmentImpl(SnapShotFragmentView view) {
        this.viewListener = view;
        this.retrofitInteractor = new RetrofitInteractorImpl();
    }

    @Override
    public void processDataPass(ProductPass productPass) {
        if (productPass.haveBasicData()) viewListener.renderTempProductData(productPass);
    }

    @Override
    public void requestProductDetail(final Context context, final ProductPass productPass,
                                     int type,
                                     boolean forceNetwork) {
        viewListener.showProgressLoading();
        getProductDetailFromNetwork(context, productPass);
    }

    private void getProductDetailFromNetwork(final Context context, ProductPass productPass) {
        retrofitInteractor.getProductDetail(context, NetworkParam.paramProductDetailTest2(productPass),
                new RetrofitInteractor.ProductDetailListener() {
                    @Override
                    public void onSuccess(@NonNull ProductDetailData data) {
                        viewListener.onProductDetailLoaded(data);
                        viewListener.hideProgressLoading();
                    }

                    @Override
                    public void onTimeout() {
                        viewListener.showProductDetailRetry();
                        viewListener.hideProgressLoading();
                    }

                    @Override
                    public void onError(String error) {
                        viewListener.showProductDetailRetry();
                        viewListener.hideProgressLoading();
                    }

                    @Override
                    public void onNullData() {
                        viewListener.hideProgressLoading();
                        viewListener.onNullData();
                    }

                    @Override
                    public void onReportServerProblem() {
                        viewListener.hideProgressLoading();
                        viewListener.showFullScreenError();
                    }
                });
    }
}
