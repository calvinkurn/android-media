package com.tokopedia.gm.subscribe.view.presenter;


import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.gm.subscribe.domain.product.interactor.GetGmSubscribeCurrentProductUseCase;
import com.tokopedia.gm.subscribe.domain.product.interactor.GetGmSubscribeExtendProductUseCase;
import com.tokopedia.gm.subscribe.domain.product.model.GmProductDomainModel;
import com.tokopedia.gm.subscribe.view.fragment.GmProductView;
import com.tokopedia.gm.subscribe.view.viewmodel.GmProductViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author sebastianuskh on 11/23/16.
 */

public class GmProductPresenterImpl extends BaseDaggerPresenter<GmProductView> implements GmProductPresenter {
    private final GetGmSubscribeCurrentProductUseCase getGmSubscribeCurrentProductUseCase;
    private final GetGmSubscribeExtendProductUseCase getGmSubscribeExtendProductUseCase;

    @Inject
    public GmProductPresenterImpl(GetGmSubscribeCurrentProductUseCase getGmSubscribeCurrentProductUseCase, GetGmSubscribeExtendProductUseCase getGmSubscribeExtendProductUseCase) {
        this.getGmSubscribeCurrentProductUseCase = getGmSubscribeCurrentProductUseCase;
        this.getGmSubscribeExtendProductUseCase = getGmSubscribeExtendProductUseCase;
    }

    @Override
    public void getCurrentPackageSelection() {
        checkViewAttached();
        getView().clearPackage();
        getView().showProgressDialog();
        getView().setVisibilitySelectButton(false);
        getGmSubscribeCurrentProductUseCase.execute(RequestParams.EMPTY, new ProductListSubscriber());
    }

    @Override
    public void getExtendPackageSelection() {
        checkViewAttached();
        getView().showProgressDialog();
        getView().setVisibilitySelectButton(false);
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
            getView().dismissProgressDialog();
            getView().errorGetProductList();
        }

        @Override
        public void onNext(List<GmProductDomainModel> gmProductDomainModels) {
            getView().dismissProgressDialog();
            getView().setVisibilitySelectButton(true);
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
