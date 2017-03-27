package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.seller.app.BaseDiPresenter;

/**
 * Created by zulfikarrahman on 3/20/17.
 */

public abstract class ShopSettingInfoPresenter extends BaseDiPresenter<ShopSettingInfoView> {
    public ShopSettingInfoPresenter(ShopSettingInfoView view) {
        super(view);
    }

    public abstract void submitShopInfo(String uriPathImage, String shopSlogan, String shopDescription);
}
