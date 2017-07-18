package com.tokopedia.seller;

import android.content.Context;

import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter {

    GoldMerchantComponent getGoldMerchantComponent(ActivityModule activityModule);

    void goToHome(Context context);
    void goToProductDetail(Context context, String productUrl);
}
