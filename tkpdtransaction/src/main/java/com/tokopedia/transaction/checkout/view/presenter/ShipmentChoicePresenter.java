package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
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
    private ShipmentItemData selectedShipment;
    private ShipmentDetailData shipmentDetailData;

    @Override
    public void attachView(IShipmentChoiceView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public void loadShipmentChoice(ShipmentDetailData shipmentDetailData, ShipmentItemData selectedShipment) {
        getView().showLoading();
        if (shipmentDetailData != null) {
            this.shipmentDetailData = shipmentDetailData;
        }
        shipments = shipmentDetailData.getShipmentItemData();
        if (selectedShipment != null) {
            this.selectedShipment = selectedShipment;
            for (int i = 0; i < shipments.size(); i++) {
                if (shipments.get(i).getId().equals(selectedShipment.getId())) {
                    shipments.get(i).setSelected(true);
                }
            }
        }
        getView().showData();
        getView().hideLoading();
    }

    @Override
    public List<ShipmentItemData> getShipmentChoices() {
        return shipments;
    }

    @Override
    public ShipmentDetailData getShipmentDetailData() {
        return shipmentDetailData;
    }

}
