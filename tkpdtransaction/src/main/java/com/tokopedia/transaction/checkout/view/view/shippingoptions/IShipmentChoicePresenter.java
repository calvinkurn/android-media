package com.tokopedia.transaction.checkout.view.view.shippingoptions;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentDetailData;
import com.tokopedia.transaction.checkout.domain.datamodel.ShipmentItemData;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentChoicePresenter extends CustomerPresenter<IShipmentChoiceView> {

    void loadShipmentChoice(ShipmentDetailData shipmentDetailData, ShipmentItemData selectedShipment);

    List<ShipmentItemData> getShipmentChoices();

    ShipmentDetailData getShipmentDetailData();
}
