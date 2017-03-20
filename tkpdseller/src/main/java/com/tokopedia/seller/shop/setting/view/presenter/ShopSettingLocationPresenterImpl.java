package com.tokopedia.seller.shop.setting.view.presenter;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public class ShopSettingLocationPresenterImpl extends ShopSettingLocationPresenter {

    public ShopSettingLocationPresenterImpl(ShopSettingLocationView view) {
        super(view);
    }

    @Override
    public void changeLocationPickup() {
        view.changeLocationString("Alallala street number 444 sgessa");
    }

    @Override
    protected void unsubscribeOnDestroy() {

    }
}
