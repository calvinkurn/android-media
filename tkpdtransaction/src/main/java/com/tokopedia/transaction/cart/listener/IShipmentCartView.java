package com.tokopedia.transaction.cart.listener;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.transaction.cart.adapter.ShipmentCartAdapter;
import com.tokopedia.transaction.cart.adapter.ShipmentPackageCartAdapter;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.calculateshipment.ShipmentPackage;

import java.util.List;


/**
 * @author anggaprasetiyo on 11/2/16.
 */

public interface IShipmentCartView {
    /**
     * untuk render spinner, render biaya
     *
     * @param data hasil dari network
     */
    void renderCalculateShipment(@NonNull List<Shipment> data);

    void renderSpinnerShipment();

    void renderGeocodeLocation(String location);

    void renderEditLocationShipment(@NonNull String message);

    void renderErrorCalculateShipment(String error);

    void renderErrorEditShipment(String error);

    void renderErrorEditLocationShipment(String error);

    boolean isLoading();

    void showLoading();

    void dismisLoading();

    void renderResultChangeAddress(@NonNull Bundle bundle);

    void renderResultChooseLocation(@NonNull Bundle bundle);

    void showGeolocationMap(ShipmentPackage shipmentPackage);

    Activity getActivity();

    void navigateToChooseAddress();

    void navigateToAddAddress();

    void navigateToCart(String message);

    void navigateToGeolocation(LocationPass locationPass);

    ShipmentCartAdapter getShipmentAdapter();

    ShipmentPackageCartAdapter getShipmentPackageAdapter();

    void renderEmptyShipment();

    void renderEmptyShipmentPackageSpinner();

    void showSnackbar(String message);

    void renderErrorEditLocationShipmentNoConnection();

    void renderErrorEditShipmentTimeout();

    void renderEditShipmentErrorSnackbar(String errorMessage);
}
