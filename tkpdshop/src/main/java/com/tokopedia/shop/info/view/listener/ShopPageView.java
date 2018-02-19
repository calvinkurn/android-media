package com.tokopedia.shop.info.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopPageView extends CustomerView {
    void renderData(SpeedReputation speedReputation);

    void renderData(final ShopInfo shopInfo);
}
