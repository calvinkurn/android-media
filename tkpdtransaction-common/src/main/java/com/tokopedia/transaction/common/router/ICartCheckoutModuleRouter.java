package com.tokopedia.transaction.common.router;

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

    Intent tkpdCartCheckoutGetLoyaltyNewCheckoutMarketplaceCartListIntent(Context context, boolean couponActive);

    Intent tkpdCartCheckoutGetLoyaltyNewCheckoutMarketplaceCartShipmentIntent(
            Context context, String additionalDataString, boolean couponActive);

    Intent tkpdCartCheckoutGetProductDetailIntent(
            Context context, ProductPass productPass
    );

    Intent tkpdCartCheckoutGetShopInfoIntent(
            Context context, String shopId
    );

    Intent navigateToInsuranceTncActivity();

    Intent navigateToPickupPointActivityFromCartMultipleAddress(Activity activity,
                                                                int cartPosition,
                                                                String districtName,
                                                                HashMap<String, String> params);

    Intent navigateToPickupPointActivityFromCartSingleAddress(Activity activity, String districtName,
                                                              HashMap<String, String> params);
}
