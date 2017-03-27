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
        if (validateForm(uriPathImage, shopSlogan, shopDescription)) {
            checkViewAttached();
            getView().showProgressDialog();
            shopSettingSaveInfoUseCase.execute(ShopSettingSaveInfoUseCase.createRequestParams(uriPathImage,
                    shopDescription, shopSlogan), getSubscriberSaveInfo());
        }
    }

    public Subscriber<Boolean> getSubscriberSaveInfo() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                checkViewAttached();
                getView().dismissProgressDialog();
                getView().onFailedSaveInfoShop();
            }

            @Override
            public void onNext(Boolean isSuccess) {
                checkViewAttached();
                if (isSuccess) {
                    getView().onSuccessSaveInfoShop();
                } else {
                    getView().onFailedSaveInfoShop();
                }
            }
        };
    }

    private boolean validateForm(String uriPathImage, String shopSlogan, String shopDescription) {
        checkViewAttached();
        if (!uriPathImage.isEmpty() && !shopSlogan.isEmpty() && !shopDescription.isEmpty()) {
            getView().onErrorDescriptionEmptyFalse();
            getView().onErrorEmptyImageFalse();
            getView().onErrorSloganEmptyFalse();
            return true;
        } else {
            if (uriPathImage.isEmpty()) {
                getView().onErrorEmptyImage();
            } else {
                getView().onErrorEmptyImageFalse();
            }

            if (shopSlogan.isEmpty()) {
                getView().onErrorSloganEmpty();
            } else {
                getView().onErrorSloganEmptyFalse();
            }

            if (shopDescription.isEmpty()) {
                getView().onErrorDescriptionEmpty();
            } else {
                getView().onErrorDescriptionEmptyFalse();
            }
            return false;
        }
    }
}
