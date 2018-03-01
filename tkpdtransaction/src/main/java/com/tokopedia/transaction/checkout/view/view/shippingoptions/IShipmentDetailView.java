package com.tokopedia.transaction.checkout.view.view.shippingoptions;

import android.app.Activity;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentDetailView extends CustomerView {

    void showLoading();

    void hideLoading();

    void showNoConnection(String message);

    void renderShipmentWithMap(ShipmentDetailData shipmentDetailData);

    void renderShipmentWithoutMap(ShipmentDetailData shipmentDetailData);

    void renderFirstLoadedRatesData(ShipmentDetailData shipmentDetailData);

    void showAllCouriers();

    void showPinPointChooserMap(ShipmentDetailData shipmentDetailData);

    void showPinPointMap(ShipmentDetailData shipmentDetailData);

    Activity getActivity();

}
