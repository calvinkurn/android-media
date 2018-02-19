package com.tokopedia.shop.info.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;
import com.tokopedia.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.shop.info.view.listener.ShopInfoView;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by nathan on 2/6/18.
 */

public class ShopInfoPresenter extends BaseDaggerPresenter<ShopInfoView> {

    private final GetShopInfoUseCase getShopInfoUseCase;

    @Inject
    public ShopInfoPresenter(GetShopInfoUseCase getShopInfoUseCase) {
        this.getShopInfoUseCase = getShopInfoUseCase;
    }

    public void getShopInfo(String shopId) {
        getShopInfoUseCase.execute(GetShopInfoUseCase.createRequestParam(shopId), new Subscriber<ShopInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onErrorGetShopInfo(e);
                }
            }

            @Override
            public void onNext(ShopInfo shopInfo) {
                getView().onSuccessGetShopInfo(shopInfo);
            }
        });
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopInfoUseCase.unsubscribe();
    }
}