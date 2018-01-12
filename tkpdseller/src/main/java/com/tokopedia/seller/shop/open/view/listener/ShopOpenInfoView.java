package com.tokopedia.seller.shop.open.view.listener;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.seller.shop.open.data.model.response.isreservedomain.ResponseIsReserveDomain;
import com.tokopedia.seller.shop.open.domain.model.ShopOpenSaveInfoResponseModel;

/**
 * Created by zulfikarrahman on 3/16/17.
 */
public interface ShopOpenInfoView extends CustomerView {

    void showProgressDialog();

    void dismissProgressDialog();

    void onSuccessSaveInfoShop(ShopOpenSaveInfoResponseModel responseModel);

    void onFailedSaveInfoShop(Throwable t);

    void onSuccessGetReserveDomain(ResponseIsReserveDomain responseIsReserveDomain);

    void onErrorGetReserveDomain(Throwable e);
}
