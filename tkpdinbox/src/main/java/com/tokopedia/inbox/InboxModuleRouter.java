package com.tokopedia.inbox;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author by alvinatin on 09/03/18.
 */

public interface InboxModuleRouter {
    String ENABLE_TOPCHAT = "enable_topchat";

    String TX_ASK_SELLER = "tx_ask_seller";
    String TX_ASK_BUYER = "tx_ask_buyer";
    String SHOP = "shop";
    String PRODUCT = "product";
    String PROFILE = "profile";


    Intent getAskBuyerIntent(Context context, String toUserId,
                             String customerName, String customSubject,
                             String customMessage, String source, String avatarUrl);

    Intent getAskSellerIntent(Context context, String toShopId,
                              String shopName, String customSubject,
                              String customMessage, String source, String avatarUrl);

    Intent getAskSellerIntent(Context context, String toShopId, String shopName, String source,
                              String avatarUrl);

    Intent getAskUserIntent(Context context, String toUserId, String userName, String source, String avatarUrl);

    Intent getTimeMachineIntent(Context context);

    Intent getInboxMessageIntent(Context context);

    Intent getContactUsIntent(Context context);

    Intent getHomeIntent(Context context);

    Intent getGalleryIntent(Context context, boolean forceOpenCamera, int maxImageSelection, boolean compressToTkpd);

    void actionNavigateByApplinksUrl(Activity activity, String url, Bundle bundle);

    Intent getTopProfileIntent(Context context, String userId);
}
