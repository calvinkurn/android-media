package com.tokopedia.seller.shop.setting.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.shop.setting.view.listener.ShopSettingLogisticView;

/**
 * Created by sebastianuskh on 3/23/17.
 */

public abstract class ShopSettingLogisticPresenter extends BaseDaggerPresenter<ShopSettingLogisticView> {

    protected abstract void unsubscribeOnDestroy();

    public abstract void updateLogistic(int districtCode);
}
