package com.tokopedia.loyalty.view.presenter;

import android.app.Activity;
import android.content.Context;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface IPromoCodePresenter {

    void processCheckPromoCode(Context context, String voucherId);

    void processCheckDigitalPromoCode(Context context, String voucherId, String categoryId);

    void processCheckMarketPlaceCartListPromoCode(Activity activity, String voucherCode);

    void processCheckMarketPlaceCartShipmentPromoCode(Activity activity, String voucherCode, String paramCartShipment);

    void processCheckFlightPromoCode(Activity activity, String voucherCode, String cartId);
}
