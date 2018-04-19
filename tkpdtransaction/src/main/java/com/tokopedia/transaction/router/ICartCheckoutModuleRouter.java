package com.tokopedia.transaction.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.router.productdetail.passdata.ProductPass;

import java.util.HashMap;

/**
 * @author anggaprasetiyo on 28/02/18.
 */

public interface ICartCheckoutModuleRouter {

    int LOYALTY_REQUEST_CODE = 77;

    Intent tkpdCartCheckoutGetLoyaltyOldCheckoutCouponActiveIntent(
            Context context, String platform, String category
    );

    Intent tkpdCartCheckoutGetLoyaltyOldCheckoutCouponNotActiveIntent(
            Context context, String platform, String category
    );

}
