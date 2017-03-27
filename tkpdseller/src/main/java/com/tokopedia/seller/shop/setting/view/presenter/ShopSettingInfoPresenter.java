package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;

/**
 * Created by zulfikarrahman on 3/20/17.
 */

public abstract class ShopSettingInfoPresenter extends BaseDaggerPresenter<ShopSettingInfoView> {

    public abstract void submitShopInfo(String uriPathImage, String shopSlogan, String shopDescription);
}
