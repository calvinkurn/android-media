package com.tokopedia.loyalty.view.view;

import com.tokopedia.loyalty.view.data.PromoData;
import com.tokopedia.loyalty.view.data.PromoMenuData;

import java.util.List;

/**
 * @author anggaprasetiyo on 04/01/18.
 */

public interface IPromoListActivityView extends IBaseView {
    void renderPromoMenuDataList(List<PromoMenuData> promoMenuDataList);

    void renderErrorGetPromoMenuDataList(String message);

    void renderErrorHttpGetPromoMenuDataList(String message);

    void renderErrorNoConnectionGetPromoMenuDataList(String message);

    void renderErrorTimeoutConnectionGetPromoMenuDataListt(String message);
}
