package com.tokopedia.seller.gmstat.presenters;

import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;
import com.tokopedia.sellerapp.home.utils.ImageHandler;

/**
 * Created by normansyahputa on 11/2/16.
 */

public interface GMStat {
    GMStatNetworkController getGmStatNetworkController();
    ImageHandler getImageHandler();
    boolean isGoldMerchant();
    String getShopId();
}
