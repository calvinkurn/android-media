package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.setting.domain.interactor.GetLogisticAvailableUseCase;
import com.tokopedia.seller.shop.setting.domain.model.LogisticAvailableDomainModel;

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
    protected void unsubscribeOnDestroy() {

    }

    @Override
    public void updateLogistic(int districtCode) {
        RequestParams requestParam = GetLogisticAvailableUseCase.generateParams(districtCode);
        getLogisticAvailableUseCase.execute(requestParam, new UpdateLogisticSubscriber());

    }

    private class UpdateLogisticSubscriber extends Subscriber<LogisticAvailableDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(LogisticAvailableDomainModel logisticAvailableDomainModel) {

        }
    }
}
