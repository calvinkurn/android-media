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
}
