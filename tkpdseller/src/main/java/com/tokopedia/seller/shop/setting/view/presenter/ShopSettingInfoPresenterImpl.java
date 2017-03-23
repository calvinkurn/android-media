package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.seller.shop.setting.domain.interactor.ShopSettingSaveInfoUseCase;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 3/20/17.
 */

public class ShopSettingInfoPresenterImpl extends ShopSettingInfoPresenter {

    ShopSettingSaveInfoUseCase shopSettingSaveInfoUseCase;

    public ShopSettingInfoPresenterImpl(ShopSettingInfoView view, ShopSettingSaveInfoUseCase shopSettingSaveInfoUseCase) {
        super(view);
        this.shopSettingSaveInfoUseCase = shopSettingSaveInfoUseCase;
    }

    @Override
    public void submitShopInfo(String uriPathImage, String shopSlogan, String shopDescription) {
        if(validateForm(uriPathImage, shopSlogan, shopDescription)) {
            shopSettingSaveInfoUseCase.execute(ShopSettingSaveInfoUseCase.createRequestParams(uriPathImage,
                    shopDescription, shopSlogan), getSubscriberSaveInfo());
        }
    }

    @Override
    protected void unsubscribeOnDestroy() {

    }

    public Subscriber<Boolean> getSubscriberSaveInfo() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean aBoolean) {

            }
        };
    }

    private boolean validateForm(String uriPathImage, String shopSlogan, String shopDescription) {
        if(!uriPathImage.isEmpty() && !shopSlogan.isEmpty() && !shopDescription.isEmpty()){
            return true;
        }else{
            if(uriPathImage.isEmpty()){
                view.onErrorEmptyImage();
            }else{
                view.onErrorEmptyImageFalse();
            }

            if(shopSlogan.isEmpty()){
                view.onErrorSloganEmpty();
            }else{
                view.onErrorSloganEmptyFalse();
            }

            if(shopDescription.isEmpty()){
                view.onErrorDescriptionEmpty();
            }else{
                view.onErrorDescriptionEmptyFalse();
            }
            return false;
        }
    }
}
