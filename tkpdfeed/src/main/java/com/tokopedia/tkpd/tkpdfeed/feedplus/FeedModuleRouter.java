package com.tokopedia.tkpd.tkpdfeed.feedplus;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;

/**
 * Created by meyta on 2/12/18.
 */

public interface FeedModuleRouter {

    Intent getLoginIntent(Context context);

    AnalyticTracker getAnalyticTracker();

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShopPageIntent(Context context, String shopId, Bundle existingBundle);
}
