package com.tokopedia.seller.shop.setting.view.listener;

import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.seller.shop.setting.view.model.ShopSettingLocationModel;

/**
 * Created by sebastianuskh on 3/22/17.
 */

public interface ShopSettingLocationListener {
    void goToPickupLocationPicker(LocationPass locationPass);

    void goToShopSettingLogisticFragment(ShopSettingLocationModel model);
}
