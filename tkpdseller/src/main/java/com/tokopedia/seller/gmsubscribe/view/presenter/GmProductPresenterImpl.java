package com.tokopedia.seller.gmsubscribe.view.presenter;


import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmSubscribeCurrentProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.interactor.GetGmSubscribeExtendProductUseCase;
import com.tokopedia.seller.gmsubscribe.domain.product.model.GmProductDomainModel;
import com.tokopedia.seller.gmsubscribe.view.fragment.GmProductView;
import com.tokopedia.seller.gmsubscribe.view.viewmodel.GmProductViewModel;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 11/23/16.
 */

public class GmProductPresenterImpl extends BaseDaggerPresenter<GmProductView> implements GmProductPresenter {
    private final GetGmSubscribeCurrentProductUseCase getGmSubscribeCurrentProductUseCase;
    private final GetGmSubscribeExtendProductUseCase getGmSubscribeExtendProductUseCase;

    public GmProductPresenterImpl(GetGmSubscribeCurrentProductUseCase getGmSubscribeCurrentProductUseCase, GetGmSubscribeExtendProductUseCase getGmSubscribeExtendProductUseCase) {
        this.getGmSubscribeCurrentProductUseCase = getGmSubscribeCurrentProductUseCase;
        this.getGmSubscribeExtendProductUseCase = getGmSubscribeExtendProductUseCase;
    }

    @Override
    public void getCurrentPackageSelection() {
        checkViewAttached();
        getView().clearPackage();
        getView().showProgressDialog();
        getGmSubscribeCurrentProductUseCase.execute(RequestParams.EMPTY, new ProductListSubscriber());
    }

    @Override
    public void getExtendPackageSelection() {
        checkViewAttached();
        getView().showProgressDialog();
        getGmSubscribeExtendProductUseCase.execute(RequestParams.EMPTY, new ProductListSubscriber());
    }

    @Override
    public void detachView() {
        super.detachView();
        getGmSubscribeCurrentProductUseCase.unsubscribe();
        getGmSubscribeExtendProductUseCase.unsubscribe();
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
