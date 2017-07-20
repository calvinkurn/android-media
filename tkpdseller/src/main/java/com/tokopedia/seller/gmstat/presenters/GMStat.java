package com.tokopedia.seller.gmstat.presenters;

import com.tkpd.library.utils.image.ImageHandler;
import com.tokopedia.seller.gmstat.utils.GMStatNetworkController;
import com.tokopedia.seller.goldmerchant.statistic.domain.GMStatRepository;

/**
 * Created by normansyahputa on 11/2/16.
 */

public interface GMStat {
    GMStatNetworkController getGmStatNetworkController();

    ImageHandler getImageHandler();

    boolean isGoldMerchant();

    String getShopId();

    GMStatRepository gmStatRepository();

    void joinRepository();
}
