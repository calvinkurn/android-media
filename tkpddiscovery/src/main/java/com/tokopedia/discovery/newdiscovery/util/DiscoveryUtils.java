package com.tokopedia.discovery.newdiscovery.util;

import android.content.Context;
import android.os.Build;
import android.provider.Settings;

/**
 * @author by errysuprayogi on 11/17/17.
 */

public class DiscoveryUtils {

    public static boolean isFinishActivitiesOptionEnabled(Context context) {
        int result;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            result = Settings.System.getInt(context.getContentResolver(), Settings.Global.ALWAYS_FINISH_ACTIVITIES, 0);
        } else {
            result = Settings.Global.getInt(context.getContentResolver(), Settings.System.ALWAYS_FINISH_ACTIVITIES, 0);
        }

        return result == 1;
    }

}
