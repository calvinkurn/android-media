package com.tokopedia.mitratoppers;

import android.content.Context;
import android.content.Intent;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface MitraToppersRouter {

    Intent getMitraToppersActivityIntent(Context context);

    void sendEventTrackingWithShopInfo(String event, String category, String action, String label,
                           String shopId, boolean isGoldMerchant, boolean isOfficialStore);
}
