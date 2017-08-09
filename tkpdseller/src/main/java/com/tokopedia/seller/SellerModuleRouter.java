package com.tokopedia.seller;

import android.content.Context;

import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter {

    GoldMerchantComponent getGoldMerchantComponent(ActivityModule activityModule);

    ProductComponent getProductComponent(ActivityModule activityModule);

    void goToHome(Context context);
    void goToProductDetail(Context context, String productUrl);
}
