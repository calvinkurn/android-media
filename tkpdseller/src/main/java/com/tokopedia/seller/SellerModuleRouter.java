package com.tokopedia.seller;

import com.tokopedia.core.base.di.module.ActivityModule;
import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter extends com.tokopedia.core.router.digitalmodule.sellermodule.SellerModuleRouter {

    GoldMerchantComponent getGoldMerchantComponent(ActivityModule activityModule);
}
