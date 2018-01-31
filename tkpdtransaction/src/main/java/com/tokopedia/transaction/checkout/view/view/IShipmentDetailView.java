package com.tokopedia.transaction.checkout.view.view;

import com.tokopedia.core.base.presentation.CustomerView;
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

    void showData();

    void renderInstantShipment(ShipmentDetailData shipmentDetailData);

    void renderSameDayShipment(ShipmentDetailData shipmentDetailData);

    void renderNextDayShipment(ShipmentDetailData shipmentDetailData);

    void renderRegularShipment(ShipmentDetailData shipmentDetailData);

    void renderKargoShipment(ShipmentDetailData shipmentDetailData);

    void showFirstThreeCouriers(List<CourierItemData> couriers);

    void showAllCouriers(List<CourierItemData> couriers);

    void showPinPointChooserMap(ShipmentDetailData shipmentDetailData);

    void showPinPointMap(ShipmentDetailData shipmentDetailData);

}
