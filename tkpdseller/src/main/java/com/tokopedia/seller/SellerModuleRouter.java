package com.tokopedia.seller;

import com.tokopedia.seller.goldmerchant.common.di.component.GoldMerchantComponent;
import com.tokopedia.seller.product.common.di.component.ProductComponent;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter extends com.tokopedia.core.router.digitalmodule.sellermodule.SellerModuleRouter {

    GoldMerchantComponent getGoldMerchantComponent();

    ProductComponent getProductComponent();
}
