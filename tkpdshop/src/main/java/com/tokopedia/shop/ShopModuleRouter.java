package com.tokopedia.shop;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

/**
 * Created by normansyahputa on 2/13/18.
 */

public interface ShopModuleRouter {

    Fragment getShopReputationFragmentShop(String shopId, String shopDomain);

    Fragment getShopTalkFragment();

    void goToManageShop(Context context);

    void goToAddProduct(Context context);

    void goToChatSeller(Context context, String shopId, String shopName, String avatar);

    void goToShareShop(Context context, String shopId, String shopUrl, String shareLabel);

    void goToProductDetail(Context context, String productUrl);

    void goToWebview(String url);

    void goToProductDetailById(Context activity, String productId);
}
