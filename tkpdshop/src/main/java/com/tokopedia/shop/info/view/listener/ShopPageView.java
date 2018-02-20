package com.tokopedia.shop.info.view.listener;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.reputation.common.data.source.cloud.model.ReputationSpeed;
import com.tokopedia.reputation.speed.SpeedReputation;
import com.tokopedia.shop.common.data.source.cloud.model.ShopInfo;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopPageView extends CustomerView {

    void onSuccessGetShopInfo(ShopInfo shopInfo);

    void onErrorGetShopInfo(Throwable e);

    void onSuccessGetReputationSpeed(ReputationSpeed reputationSpeed);

    void onErrorGetReputationSpeed(Throwable e);
}
