package com.tokopedia.transaction.checkout.view.view;

import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentDetailView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showNoConnection(String message);

    void showData();

    void renderInstantShipment(ShipmentDetailData shipmentDetailData);

    void renderRegularShipment(ShipmentDetailData shipmentDetailData);

}
