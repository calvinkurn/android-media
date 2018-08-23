package com.tokopedia.loyalty.view.presenter;

import android.app.Activity;
import android.content.Context;

import com.google.gson.JsonObject;

/**
 * @author anggaprasetiyo on 27/11/17.
 */

public interface IPromoCodePresenter {

    void processCheckPromoCode(Context context, String voucherId);

    void processCheckDigitalPromoCode(Context context, String voucherId, String categoryId);

    void processCheckMarketPlaceCartListPromoCode(Activity activity, String voucherCode, String paramUpdateCart);

    void processCheckEventPromoCode(String voucherId, JsonObject requestBody, boolean flag);

    void processCheckFlightPromoCode(Activity activity, String voucherCode, String cartId);

    void processCheckDealPromoCode(String voucherId,JsonObject requestBody, boolean flag);

    void processCheckTrainPromoCode(Activity activity, String trainReservationId,
                                    String trainReservationCode, String voucherCode);

}
