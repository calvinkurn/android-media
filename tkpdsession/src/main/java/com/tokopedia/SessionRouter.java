package com.tokopedia;

import android.content.Context;
import android.content.Intent;

/**
 * @author by nisie on 10/19/17.
 */

public interface SessionRouter {
    Intent getHomeIntent(Context context);
}
