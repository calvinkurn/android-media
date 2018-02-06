package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.view.IShipmentChoiceView;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentChoicePresenter extends CustomerPresenter<IShipmentChoiceView> {

    void loadShipmentChoice(ShipmentDetailData shipmentDetailData, ShipmentItemData selectedShipment);

    List<ShipmentItemData> getShipmentChoices();

    ShipmentDetailData getShipmentDetailData();
}
