package com.tokopedia.transaction.cart.listener;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.transaction.cart.adapter.ShipmentCartAdapter;
import com.tokopedia.transaction.cart.adapter.ShipmentPackageCartAdapter;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentData;
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
    void renderCalculateShipment(@NonNull CalculateShipmentData data);

    void renderSpinnerShipment();

    void renderCostShipment();

    void renderGeocodeLocation(String location);

    void renderErrorCalculateShipment(String error);

    void renderErrorEditShipment(String error);

    void showLoading();

    void dismisLoading();

    void renderResultChangeAddress(@NonNull Bundle bundle);

    void showGeolocationMap();

    Activity getActivity();

    void navigateToChooseAddress();

    void navigateToAddAddress();

    void navigateToCart(String message);

    ShipmentCartAdapter getShipmentAdapter();

    ShipmentPackageCartAdapter getShipmentPackageAdapter();
}
