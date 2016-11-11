package com.tokopedia.transaction.cart.listener;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartData;


/**
 * @author anggaprasetiyo on 11/2/16.
 */

public interface IShipmentCartView {
    /**
     * untuk render spinner, render biaya
     *
     * @param data hasil dari network
     */
    void renderShipmentCart(@NonNull ShipmentCartData data);

    void renderSpinnerShipment();

    void renderCostShipment();

    void renderErrorCalculateShipment(String error);

    void showLoading();

    void dismisLoading();

    void renderResultChangeAddress(@NonNull Bundle bundle);

    void showGeolocationMap();

    Activity getActivity();

    void navigateToChooseAddress();

    void navigateToAddAddress();
}
