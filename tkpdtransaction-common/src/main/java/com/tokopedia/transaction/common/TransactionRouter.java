package com.tokopedia.transaction.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.transaction.common.sharedata.AddToCartRequest;
import com.tokopedia.transaction.common.sharedata.AddToCartResult;

import rx.Observable;

/**
 * Created by kris on 7/21/17. Tokopedia
 */

public interface TransactionRouter {

    void goToUserPaymentList(Activity activity);

    void goToOrderHistory(Context context, String orderId, int userMode);

    Intent goToOrderDetail(Context context, String orderId);

    Intent getInboxReputationIntent(Context context);

    Intent getDetailResChatIntentBuyer(Context context, String resoId, String shopName);

    Intent getResolutionCenterIntent(Context context);

    Intent getShopPageIntent(Context context, String shopId);

    Intent getShoProductListIntent(Context context, String shopId, String keyword, String etalaseId);

    boolean getEnableFingerprintPayment();

    Observable<AddToCartResult> addToCartProduct(AddToCartRequest addToCartRequest, boolean isOneClickShipment);

    void updateMarketplaceCartCounter(CartNotificationListener listener);

    interface CartNotificationListener {
        void onDataReady();
    }

    void setCartCount(Context context, int count);

    int getCartCount(Context context);
}
