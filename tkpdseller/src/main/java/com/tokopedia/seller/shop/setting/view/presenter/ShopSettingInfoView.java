package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by zulfikarrahman on 3/16/17.
 */
public interface ShopSettingInfoView extends CustomerView {
    void onErrorEmptyImage();

    void onErrorEmptyImageFalse();

    void onErrorSloganEmpty();

    void onErrorSloganEmptyFalse();

    void onErrorDescriptionEmpty();

    void onErrorDescriptionEmptyFalse();

    void showProgressDialog();

    void dismissProgressDialog();

    void onSuccessSaveInfoShop();

    void onFailedSaveInfoShop();
}
