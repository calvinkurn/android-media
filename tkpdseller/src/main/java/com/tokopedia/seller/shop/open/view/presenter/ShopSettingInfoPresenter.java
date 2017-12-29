package com.tokopedia.seller.shop.open.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shop.open.view.listener.ShopSettingInfoView;

/**
 * Created by zulfikarrahman on 3/20/17.
 */

public abstract class ShopSettingInfoPresenter extends BaseDaggerPresenter<ShopSettingInfoView> {

    public abstract void submitShopInfo(String uriPathImage, String shopSlogan, String shopDescription, String imageUrl, String serverId, String picObj);

    public abstract void getisReserveDomain();
}
