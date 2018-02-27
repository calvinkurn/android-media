package com.tokopedia.transaction.checkout.view.view.shippingoptions;

import android.content.Context;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentDetailPresenter extends CustomerPresenter<IShipmentDetailView> {

    void loadShipmentData(ShipmentDetailData shipmentDetailData);

    void loadAllCourier();

    List<CourierItemData> getCouriers();

    void getPinPointMapData();

    ShipmentDetailData getShipmentDetailData();

    CourierItemData getSelectedCourier();

    ShipmentItemData getSelectedShipment();

    void setSelectedShipment(ShipmentItemData selectedShipment);

    void setSelectedCourier(CourierItemData selectedCourier);

    void setCourierList(List<CourierItemData> couriers);

    void updatePinPoint(LocationPass locationPass);

}
