package com.tokopedia.seller.common.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

public class PlayStoreUtil {
    private static final String PACKAGE_SELLER_APP = "com.tokopedia.sellerapp";
    private static final String APPLINK_PLAYSTORE = "market://details?id=";
    private static final String URL_PLAYSTORE = "https://play.google.com/store/apps/details?id=";

    public static void openPlayStore(Activity activity) {
        try {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(APPLINK_PLAYSTORE + PACKAGE_SELLER_APP)
                    )
            );
        } catch (android.content.ActivityNotFoundException anfe) {
            activity.startActivity(
                    new Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(URL_PLAYSTORE + PACKAGE_SELLER_APP)
                    )
            );
        }
    }
}
