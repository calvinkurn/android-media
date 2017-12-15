package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shop.open.domain.interactor.CheckDomainNameUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.CheckShopNameUseCase;
import com.tokopedia.seller.shop.open.domain.interactor.ShopIsReserveDomainUseCase;
import com.tokopedia.seller.shop.open.view.listener.ShopCheckDomainView;
import com.tokopedia.seller.shop.setting.data.model.response.ResponseIsReserveDomain;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public class ShopCheckIsReservePresenterImpl extends BaseDaggerPresenter<ShopCheckDomainView>
        implements ShopCheckIsReservePresenter {

    private final ShopIsReserveDomainUseCase shopIsReserveDomainUseCase;

    @Inject
    public ShopCheckIsReservePresenterImpl(ShopIsReserveDomainUseCase shopIsReserveDomainUseCase) {
        this.shopIsReserveDomainUseCase = shopIsReserveDomainUseCase;
    }

    @Override
    public void isReservingDomain() {
        shopIsReserveDomainUseCase.execute(null, new Subscriber<ResponseIsReserveDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorCheckReserveDomain(e);
                }
            }

            @Override
            public void onNext(ResponseIsReserveDomain responseIsReserveDomain) {
                getView().onSuccessCheckReserveDomain(responseIsReserveDomain);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        shopIsReserveDomainUseCase.unsubscribe();
    }
}
