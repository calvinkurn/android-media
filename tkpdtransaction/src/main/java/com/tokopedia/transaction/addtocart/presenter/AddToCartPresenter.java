package com.tokopedia.transaction.addtocart.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.router.transactionmodule.passdata.ProductCartPass;
import com.tokopedia.transaction.addtocart.model.OrderData;
import com.tokopedia.transaction.addtocart.model.responseatcform.AtcFormData;
import com.tokopedia.transaction.addtocart.model.responseatcform.Destination;
import com.tokopedia.transaction.addtocart.receiver.ATCResultReceiver;

/**
 * Created by Angga.Prasetiyo on 11/03/2016.
 * Edited by Hafizh :: Kero Rates
 */
public interface AddToCartPresenter {

    void getCartFormData(@NonNull Context context, @NonNull ProductCartPass data);

    void getCartKeroToken(@NonNull Context context, @NonNull ProductCartPass data, @NonNull Destination destination);

    void calculateProduct(@NonNull Context context, @NonNull OrderData orderData, boolean mustReCalculateAddressShipping);

    void calculateKeroRates(@NonNull Context context, @NonNull AtcFormData atcFormData);

    void calculateKeroAddressShipping(@NonNull Context context, @NonNull OrderData orderData);

    Destination generateAddressData(Intent data);

    void calculateAllPrices(@NonNull Context context, @NonNull OrderData orderData);

    void processChooseGeoLocation(@NonNull Context context, @NonNull OrderData orderData);

    void updateAddressShipping(@NonNull Context context,
                               @NonNull OrderData orderData,
                               @NonNull LocationPass location);

    @Deprecated
    @SuppressWarnings("unused")
    void addToCart(@NonNull Context context, @NonNull OrderData orderData);

    boolean isValidOrder(@NonNull Context context, @NonNull OrderData orderData);

    void sendAddToCartCheckoutAnalytic(@NonNull Context context,
                                       @NonNull ProductCartPass productCartPass,
                                       String quantity,
                                       @NonNull Bundle analyticBundle);

    void processAddToCartSuccess(@NonNull Context context, String message);

    @SuppressWarnings("unused")
    void processAddToCartFailure(@NonNull Context context, String string);

    void sendAnalyticsATCSuccess(@NonNull Context context, @NonNull ProductCartPass productCartPass,
                                 @NonNull OrderData orderData);

    void addToCartService(@NonNull Context context, @NonNull ATCResultReceiver atcReceiver,
                          @NonNull OrderData orderData);

    void setCacheCart(@NonNull Context context);

    void sendToGTM(@NonNull Context context);

    void sendAppsFlyerATC(@NonNull Context context, @NonNull OrderData orderData);

    void onViewDestroyed();

    void processGetGTMTicker();

    boolean isAllowKeroAccess(AtcFormData data);

    String calculateWeight(String initWeight, String quantity);
}
