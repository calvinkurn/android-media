package com.tokopedia.seller.shop.setting.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by zulfikarrahman on 3/16/17.
 */
public interface ShopSettingInfoView extends CustomerView {

    void showProgressDialog();

    void dismissProgressDialog();

    void onSuccessSaveInfoShop();

    void onFailedSaveInfoShop(Throwable t);
}
