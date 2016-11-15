package com.tokopedia.transaction.cart.presenter;


import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentWrapper;
import com.tokopedia.transaction.cart.model.savelocation.LocationData;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationWrapper;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartWrapper;

/**
 * @author anggaprasetiyo on 11/2/16.
 */

public interface IShipmentCartPresenter {
    void goToChangeAddress();

    void goToAddAddress();

    void goToGeolocation(LocationData data);

    void backToCart();

    void processGeoCodeLocation(LocationPass locationPass);

    void processCalculateShipment(CalculateShipmentWrapper wrapper);

    void processEditShipmentCart(ShipmentCartWrapper wrapper);

    void processSaveLocationShipment(SaveLocationWrapper wrapper);
}
