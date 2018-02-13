package com.tokopedia.shop.info.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.interfaces.merchant.shop.info.ShopInfo;
import com.tokopedia.reputation.speed.SpeedReputation;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopPageView extends CustomerView {
    void renderData(SpeedReputation speedReputation);

    void renderData(final ShopInfo shopInfo);
}
