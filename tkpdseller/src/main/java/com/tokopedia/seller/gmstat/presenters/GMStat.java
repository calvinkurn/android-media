package com.tokopedia.seller.gmstat.presenters;

import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;

/**
 * Created by normansyahputa on 11/2/16.
 */

public interface GMStat {
    GMStatNetworkController getGmStatNetworkController();

    boolean isGoldMerchant();

    String getShopId();
}
