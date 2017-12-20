package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLogisticView;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public abstract class ShopSettingLogisticPresenter extends BaseDaggerPresenter<ShopSettingLogisticView> {

    public abstract void getCouriers(int districtCode);
}
