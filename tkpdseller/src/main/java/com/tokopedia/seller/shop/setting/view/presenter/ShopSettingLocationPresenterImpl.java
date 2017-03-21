package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.shop.setting.domain.interactor.FetchDistrictDataUseCase;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public class ShopSettingLocationPresenterImpl extends ShopSettingLocationPresenter {

    private final FetchDistrictDataUseCase fetchDistrictDataUseCase;

    public ShopSettingLocationPresenterImpl(ShopSettingLocationView view, FetchDistrictDataUseCase fetchDistrictDataUseCase) {
        super(view);
        this.fetchDistrictDataUseCase = fetchDistrictDataUseCase;
    }

    @Override
    public void getDistrictData() {
        fetchDistrictDataUseCase.execute(RequestParams.EMPTY, new GetDistrictDataSubscriber());
    }

    @Override
    protected void unsubscribeOnDestroy() {
        fetchDistrictDataUseCase.unsubscribe();

    }

    private class GetDistrictDataSubscriber extends Subscriber<Boolean> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Boolean aBoolean) {

        }
    }
}
