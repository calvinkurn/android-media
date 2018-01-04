package com.tokopedia.loyalty.view.view;

import com.tokopedia.loyalty.view.data.PromoData;

import java.util.List;

/**
 * @author anggaprasetiyo on 02/01/18.
 */

public interface IPromoListView extends IBaseView {

    void renderPromoDataList(List<PromoData> couponData);

    void renderErrorGetPromoDataList(String message);

    void renderErrorHttpGetPromoDataList(String message);

    void renderErrorNoConnectionGetPromoDataList(String message);

    void renderErrorTimeoutConnectionGetPromoDataListt(String message);

}
