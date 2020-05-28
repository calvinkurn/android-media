package com.tokopedia.seller;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.seller.shop.common.di.component.ShopComponent;

/**
 * Created by normansyahputa on 12/14/16.
 */

public interface SellerModuleRouter {

    ShopComponent getShopComponent();

    Intent getPhoneVerificationActivityIntent(Context context);
}