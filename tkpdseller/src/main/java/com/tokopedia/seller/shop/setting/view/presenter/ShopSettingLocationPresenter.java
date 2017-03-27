package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;

/**
 * Created by sebastianuskh on 3/17/17.
 */

public abstract class ShopSettingLocationPresenter extends BaseDaggerPresenter<ShopSettingLocationView> {

    public abstract void getDistrictData();

    public abstract void getRecomendationLocationDistrict(String stringTyped);

    protected abstract void unsubscribeOnDestroy();
}
