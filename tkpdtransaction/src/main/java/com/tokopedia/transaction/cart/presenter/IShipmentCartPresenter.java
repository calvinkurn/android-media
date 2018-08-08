package com.tokopedia.transaction.cart.presenter;


import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentWrapper;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationWrapper;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartWrapper;

/**
 * @author anggaprasetiyo on 11/2/16.
 *         modified by alvarisi
 */

public interface IShipmentCartPresenter {

    void processGeoCodeLocation(LocationPass locationPass);

    void processCalculateShipment(CalculateShipmentWrapper wrapper);

    void processEditShipmentCart(ShipmentCartWrapper wrapper);

    void processSaveLocationShipment(SaveLocationWrapper wrapper);

    void destroy();
}
