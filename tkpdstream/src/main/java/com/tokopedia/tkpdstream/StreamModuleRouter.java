package com.tokopedia.tkpdstream;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 3/1/18.
 */

public interface StreamModuleRouter {

    Intent getHomeIntent(Context context);

    Intent getInboxChannelsIntent(Context context);
}
