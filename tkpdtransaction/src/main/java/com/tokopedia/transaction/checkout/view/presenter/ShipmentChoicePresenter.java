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
        getView().showLoading();
        shipments = DummyCreator.createDummyShipmentChoices();
        getView().showData();
        getView().hideLoading();
    }

    @Override
    public List<ShipmentItemData> getShipmentChoices() {
        return shipments;
    }


    private static class DummyCreator {

        private static String[] shipmentItemDataIds = {"0", "1", "2", "3", "4"};
        private static String[] shipmentItemDataTypes = {"Instan", "Same Day", "Next Day", "Reguler", "Kargo"};
        private static String[] shipmentItemDataPriceRanges = {"Rp 40.00 - Rp 55.000", "Rp 20.000",
                "Rp 18.000", "Rp 9.000", "Rp 20.000"};
        private static String[] shipmentItemDataDeliveryTimeRanges = {"4 jam perjalanan", "6-8 jam perjalanan",
                "1-2 hari kerja", "2-7 hari kerja", "7-10 hari kerja"};

        private static List<ShipmentItemData> createDummyShipmentChoices() {
            List<ShipmentItemData> shipments = new ArrayList<>();

            for(int i=0; i<shipmentItemDataIds.length; i++){
                ShipmentItemData shipmentItemData = new ShipmentItemData();
                shipmentItemData.setId(shipmentItemDataIds[i]);
                shipmentItemData.setType(shipmentItemDataTypes[i]);
                shipmentItemData.setPriceRange(shipmentItemDataPriceRanges[i]);
                shipmentItemData.setDeliveryTimeRange(shipmentItemDataDeliveryTimeRanges[i]);

                shipments.add(shipmentItemData);
            }

            return shipments;
        }

    }

}
