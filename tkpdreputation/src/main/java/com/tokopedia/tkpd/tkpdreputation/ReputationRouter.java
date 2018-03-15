package com.tokopedia.tkpd.tkpdreputation;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.core.gcm.model.NotificationPass;

import java.util.HashMap;

/**
 * @author by nisie on 9/20/17.
 */

public interface ReputationRouter {
    Intent getInboxReputationIntent(Context context);

    Fragment getReputationHistoryFragment();

    Intent getLoginIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    void sendEventTrackingShopPage(HashMap<String, Object> eventTracking);
}
