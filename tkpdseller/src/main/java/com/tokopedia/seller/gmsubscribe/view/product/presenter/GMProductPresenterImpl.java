package com.tokopedia.seller.gmsubscribe.view.product.presenter;


import android.util.Log;

import com.tokopedia.seller.common.domain.RequestParams;
import com.tokopedia.seller.common.presentation.BasePresenter;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGMSubscribeCurrentProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGMSubscribeExtendProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmProductDomainModel;
import com.tokopedia.seller.gmsubscribe.view.product.viewmodel.GmProductViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public class GmProductPresenterImpl extends BasePresenter<GmProductView> implements GmProductPresenter {
    private final GetGMSubscribeCurrentProductUseCase getGMSubscribeCurrentProductUseCase;
    private final GetGMSubscribeExtendProductUseCase getGMSubscribeExtendProductUseCase;

    public GmProductPresenterImpl(GetGMSubscribeCurrentProductUseCase getGMSubscribeCurrentProductUseCase, GetGMSubscribeExtendProductUseCase getGMSubscribeExtendProductUseCase) {
        this.getGMSubscribeCurrentProductUseCase = getGMSubscribeCurrentProductUseCase;
        this.getGMSubscribeExtendProductUseCase = getGMSubscribeExtendProductUseCase;
    }

    @Override
    public void getCurrentPackageSelection() {
        checkViewAttached();
        getView().clearPackage();
        getView().showProgressDialog();
        getGMSubscribeCurrentProductUseCase.execute(RequestParams.EMPTY, new ProductListSubscriber());
    }

    @Override
    public void getExtendPackageSelection() {
        checkViewAttached();
        getView().showProgressDialog();
        getGMSubscribeExtendProductUseCase.execute(RequestParams.EMPTY, new ProductListSubscriber());
    }

    @Override
    public void detachView() {
        super.detachView();
        getGMSubscribeCurrentProductUseCase.unsubscribe();
        getGMSubscribeExtendProductUseCase.unsubscribe();
    }

    private class ProductListSubscriber extends Subscriber<List<GmProductDomainModel>> {

        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
            Log.e(TAG, "error here");
            getView().dismissProgressDialog();
            getView().errorGetProductList();
        }

        @Override
        public void onNext(List<GmProductDomainModel> gmProductDomainModels) {
            Log.d(TAG, "Present the data");
            getView().dismissProgressDialog();
            if (isViewAttached()) {
                List<GmProductViewModel> viewModels = new ArrayList<>();
                for (GmProductDomainModel domainModel : gmProductDomainModels) {
                    viewModels.add(new GmProductViewModel(domainModel));
                }
                getView().renderProductList(viewModels);
            }
        }
    }
}
