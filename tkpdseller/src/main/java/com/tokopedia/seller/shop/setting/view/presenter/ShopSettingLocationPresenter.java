package com.tokopedia.seller.shop.setting.view.presenter;

import android.text.Editable;

import com.tokopedia.seller.app.BaseDiPresenter;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public abstract class ShopSettingLocationPresenter extends BaseDiPresenter<ShopSettingLocationView> {

    public ShopSettingLocationPresenter(ShopSettingLocationView view) {
        super(view);
    }

    public abstract void getDistrictData();

    public abstract void getRecomendationLocationDistrict(String stringTyped);
}
