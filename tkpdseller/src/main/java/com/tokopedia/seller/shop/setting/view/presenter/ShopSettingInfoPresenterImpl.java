package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.seller.shop.setting.domain.interactor.ShopSettingSaveInfoUseCase;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 3/20/17.
 */

public class ShopSettingInfoPresenterImpl extends ShopSettingInfoPresenter {

    ShopSettingSaveInfoUseCase shopSettingSaveInfoUseCase;

    public ShopSettingInfoPresenterImpl(ShopSettingSaveInfoUseCase shopSettingSaveInfoUseCase) {
        this.shopSettingSaveInfoUseCase = shopSettingSaveInfoUseCase;
    }

    @Override
    public void submitShopInfo(String uriPathImage, String shopSlogan, String shopDescription) {
        getView().showProgressDialog();
        shopSettingSaveInfoUseCase.execute(ShopSettingSaveInfoUseCase.createRequestParams(uriPathImage,
                shopDescription, shopSlogan), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().dismissProgressDialog();
                    getView().onFailedSaveInfoShop(e);
                }
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().dismissProgressDialog();
                if (isSuccess) {
                    getView().onSuccessSaveInfoShop();
                } else {
                    onError(new Exception());
                }
            }
        });
    }
}
