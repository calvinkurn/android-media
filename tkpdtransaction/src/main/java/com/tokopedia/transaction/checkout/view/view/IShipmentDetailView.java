package com.tokopedia.transaction.checkout.view.view;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentDetailView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showNoConnection(String message);

    void renderShipmentWithMap(ShipmentDetailData shipmentDetailData);

    void renderShipmentWithoutMap(ShipmentDetailData shipmentDetailData);

    void showAllCouriers(List<CourierItemData> couriers);

    void showPinPointChooserMap(ShipmentDetailData shipmentDetailData);

    void showPinPointMap(ShipmentDetailData shipmentDetailData);

}
