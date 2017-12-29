package com.tokopedia.seller.common.imageeditor.view;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;

/**
 * Created by User on 10/20/2017.
 */

public interface WatermarkPresenterView extends CustomerView {

    void onSuccessGetShopInfo(ShopModel shopModel);

    void onErrorGetShopInfo(Throwable t);

}
