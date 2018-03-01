package com.tokopedia.shop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {

    Fragment getShopReputationFragmentShop(String shopId, String shopDomain);

    Fragment getShopTalkFragment();

    @Deprecated
    Intent getEtalaseIntent(Context context, String shopId, int currentChoosen);

    boolean isMyOwnShop(String shopId);

    void goToManageShop(Context context);

    void goToAddProduct(Context context);

    void goToChatSeller(Context context, String shopId, String shopName);

    void goToShareShop(Context context, String shopId, String shopUrl, String shareLabel);
}
