package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.product.model.CourierItem;
import com.tokopedia.transaction.checkout.view.data.CartItemData;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentDetailPresenter extends CustomerPresenter<IShipmentDetailView> {

    void loadShipmentData();

    void loadFirstThreeCourier();

    void loadAllCourier();

    void getPinPointMapData();

    ShipmentDetailData getShipmentDetailData();

    CourierItemData getSelectedCourier();

    void setSelectedCourier(CourierItemData selectedCourier);

    void setCourierList(List<CourierItemData> couriers);

    void updatePinPoint(LocationPass locationPass);

}
