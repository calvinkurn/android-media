package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.view.IShipmentChoiceView;

import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public interface IShipmentChoicePresenter extends CustomerPresenter<IShipmentChoiceView> {

    void loadShipmentChoice();

    List<ShipmentItemData> getShipmentChoices();

}
