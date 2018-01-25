package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.view.IShipmentChoiceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentChoicePresenter extends BaseDaggerPresenter<IShipmentChoiceView>
        implements IShipmentChoicePresenter {

    private List<ShipmentItemData> shipments = new ArrayList<>();

    @Override
    public void attachView(IShipmentChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void loadShipmentChoice() {

    }

    @Override
    public List<ShipmentItemData> getShipmentChoices() {
        return shipments;
    }
}
