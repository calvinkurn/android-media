package com.tokopedia.seller.shop.setting.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.shop.open.data.model.response.ResponseIsReserveDomain;

/**
 * Created by zulfikarrahman on 3/16/17.
 */
public interface ShopSettingInfoView extends CustomerView {

    void showProgressDialog();

    void dismissProgressDialog();

    void onSuccessSaveInfoShop();

    void onFailedSaveInfoShop(Throwable t);

    void onSuccessGetReserveDomain(ResponseIsReserveDomain responseIsReserveDomain);

    void onErrorGetReserveDomain(Throwable e);
}
