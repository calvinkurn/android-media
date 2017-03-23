package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.seller.app.BaseDiView;

/**
 * Created by zulfikarrahman on 3/16/17.
 */
public interface ShopSettingInfoView extends BaseDiView {
    void onErrorEmptyImage();

    void onErrorEmptyImageFalse();

    void onErrorSloganEmpty();

    void onErrorSloganEmptyFalse();

    void onErrorDescriptionEmpty();

    void onErrorDescriptionEmptyFalse();
}
