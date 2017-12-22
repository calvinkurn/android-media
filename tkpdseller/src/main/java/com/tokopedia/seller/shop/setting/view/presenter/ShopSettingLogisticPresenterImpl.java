package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.open.data.model.OpenShopCouriersModel;
import com.tokopedia.seller.shop.setting.domain.interactor.GetLogisticAvailableUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public class ShopSettingLogisticPresenterImpl extends ShopSettingLogisticPresenter {

    private final GetLogisticAvailableUseCase getLogisticAvailableUseCase;

    @Inject
    public ShopSettingLogisticPresenterImpl(GetLogisticAvailableUseCase getLogisticAvailableUseCase) {
        this.getLogisticAvailableUseCase = getLogisticAvailableUseCase;
    }

    @Override
    public void getCouriers(int districtCode) {
        RequestParams requestParam = GetLogisticAvailableUseCase.generateParams(districtCode);
        getLogisticAvailableUseCase.execute(requestParam, new Subscriber<OpenShopCouriersModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorLoadLogistic(e);
                }
            }

            @Override
            public void onNext(OpenShopCouriersModel openShopCouriersModel) {
                getView().onSuccessLoadLogistic(openShopCouriersModel);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getLogisticAvailableUseCase.unsubscribe();
    }
}