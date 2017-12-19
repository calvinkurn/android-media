package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.open.data.model.OpenShopLogisticModel;
import com.tokopedia.seller.shop.setting.domain.interactor.GetLogisticAvailableUseCase;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public class ShopSettingLogisticPresenterImpl extends ShopSettingLogisticPresenter {

    private final GetLogisticAvailableUseCase getLogisticAvailableUseCase;

    public ShopSettingLogisticPresenterImpl(GetLogisticAvailableUseCase getLogisticAvailableUseCase) {
        this.getLogisticAvailableUseCase = getLogisticAvailableUseCase;
    }

    @Override
    public void updateLogistic(int districtCode) {
        RequestParams requestParam = GetLogisticAvailableUseCase.generateParams(districtCode);
        getLogisticAvailableUseCase.execute(requestParam, new Subscriber<OpenShopLogisticModel>() {
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
            public void onNext(OpenShopLogisticModel openShopLogisticModel) {
                getView().onSuccessLoadLogistic(openShopLogisticModel);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getLogisticAvailableUseCase.unsubscribe();
    }
}