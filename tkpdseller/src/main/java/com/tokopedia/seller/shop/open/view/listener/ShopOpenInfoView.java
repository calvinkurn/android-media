package com.tokopedia.seller.shop.open.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;

/**
 * Created by zulfikarrahman on 3/16/17.
 */
public interface ShopOpenInfoView extends CustomerView {

    void showProgressDialog();

    void dismissProgressDialog();

    void onSuccessSaveInfoShop();

    void onFailedSaveInfoShop(Throwable t);

    void onSuccessGetReserveDomain(ResponseIsReserveDomain responseIsReserveDomain);

    void onErrorGetReserveDomain(Throwable e);
}
