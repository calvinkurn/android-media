package com.tokopedia.shop;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import java.util.HashMap;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {

    Fragment getShopReputationFragmentShop(String shopId, String shopDomain);

    Fragment getShopTalkFragment();

    void goToManageShop(Context context);

    void goToEditShopNote(Context context);

    void goToAddProduct(Context context);

    void goToChatSeller(Context context, String shopId, String shopName, String avatar);

    void goToShareShop(Context context, String shopId, String shopUrl, String shareLabel);

    void goToProductDetail(Context context, String productUrl);

    void goToWebview(Context context, String url);

    void goToProductDetailById(Context activity, String productId);

    void goToProfileShop(Context context, String userId);

    Intent getLoginIntent(Context context);

    void sendEventTrackingShopPage(HashMap<String, Object> eventTracking);
}
