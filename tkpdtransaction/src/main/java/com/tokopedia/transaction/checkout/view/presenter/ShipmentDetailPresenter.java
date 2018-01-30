package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentDetailPresenter extends BaseDaggerPresenter<IShipmentDetailView>
        implements IShipmentDetailPresenter {

    private ShipmentDetailData shipmentDetailData;
    private CourierItemData selectedCourier;
    private List<CourierItemData> couriers = new ArrayList<>();

    @Override
    public void attachView(IShipmentDetailView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {

    }

    @Override
    public ShipmentDetailData getShipmentDetailData() {
        return shipmentDetailData;
    }

    @Override
    public CourierItemData getSelectedCourier() {
        return selectedCourier;
    }

    @Override
    public void setSelectedCourier(CourierItemData selectedCourier) {
        this.selectedCourier = selectedCourier;
    }

    @Override
    public void setCourierList(List<CourierItemData> couriers) {
        this.couriers = couriers;
        if (couriers.size() > 3) {
            loadFirstThreeCourier();
        } else {
            loadAllCourier();
        }
    }

    @Override
    public void updatePinPoint(LocationPass locationPass) {
        shipmentDetailData.setLatitude(Double.parseDouble(locationPass.getLatitude()));
        shipmentDetailData.setLongitude(Double.parseDouble(locationPass.getLongitude()));
        shipmentDetailData.setAddress(locationPass.getGeneratedAddress());
        getView().showPinPointMap(shipmentDetailData);
    }

    @Override
    public void loadShipmentData() {
        shipmentDetailData = DummyCreator.createDummyInstantShipmentDetailData();
        getView().renderInstantShipment(shipmentDetailData);
//        getView().renderSameDayShipment(DummyCreator.createDummySameDayShipmentDetailData());
//        getView().renderNextDayShipment(DummyCreator.createDummyNextDayShipmentDetailData());
//        getView().renderRegularShipment(DummyCreator.createDummyRegularShipmentDetailData());
//        getView().renderKargoShipment(DummyCreator.createDummyKargoShipmentDetailData());
    }

    @Override
    public void loadFirstThreeCourier() {
        getView().showFirstThreeCouriers(couriers.subList(0, 3));
    }

    @Override
    public void loadAllCourier() {
        for (int i = 3; i < couriers.size(); i++) {
            couriers.get(i).setSelected(false);
            if (i == 3) {
                getView().disableInsuranceView();
            }
        }
        getView().showAllCouriers(couriers);
    }

    @Override
    public void getPinPointMapData() {
        if (shipmentDetailData != null) {
            getView().showPinPointChooserMap(shipmentDetailData);
        }
    }


    private static class DummyCreator {

        private static final String ID = "0";
        private static final String ADDRESS = "Wisma 77 - Jalan Letjen S. Parman, Palmerah,11410";
        private static final Double LATITUDE = -6.190251;
        private static final Double LONGITUDE = 106.798920;

        private static ShipmentDetailData createDummyInstantShipmentDetailData() {
            ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
            shipmentDetailData.setId(ID);
            shipmentDetailData.setAddress(ADDRESS);
            shipmentDetailData.setLatitude(LATITUDE);
            shipmentDetailData.setLongitude(LONGITUDE);
            shipmentDetailData.setDropshipperInfo("Dropshipper Bottomsheet Info");
            shipmentDetailData.setPartialOrderInfo("Partial Order Bottomsheet Info");
            shipmentDetailData.setShipmentInfo("Kurir toko adalah layanan pengiriman dengan menggunakan kurir dari toko terkait");
            shipmentDetailData.setShipmentItemData(ShipmentChoicePresenter.DummyCreator.createDummyShipmentChoices());
            shipmentDetailData.setDeliveryPriceTotal("Rp 299.000");

            return shipmentDetailData;
        }

        private static ShipmentDetailData createDummySameDayShipmentDetailData() {
            ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
            shipmentDetailData.setId(ID);
            shipmentDetailData.setAddress(ADDRESS);
            shipmentDetailData.setLatitude(LATITUDE);
            shipmentDetailData.setLongitude(LONGITUDE);
            shipmentDetailData.setShipmentItemData(ShipmentChoicePresenter.DummyCreator.createDummyShipmentChoices());

            return shipmentDetailData;
        }

        private static ShipmentDetailData createDummyNextDayShipmentDetailData() {
            ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
            shipmentDetailData.setId(ID);
            shipmentDetailData.setAddress(ADDRESS);
            shipmentDetailData.setLatitude(LATITUDE);
            shipmentDetailData.setLongitude(LONGITUDE);
            shipmentDetailData.setShipmentItemData(ShipmentChoicePresenter.DummyCreator.createDummyShipmentChoices());

            return shipmentDetailData;
        }

        private static ShipmentDetailData createDummyRegularShipmentDetailData() {
            ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
            shipmentDetailData.setId(ID);
            shipmentDetailData.setAddress(ADDRESS);
            shipmentDetailData.setShipmentItemData(ShipmentChoicePresenter.DummyCreator.createDummyShipmentChoices());

            return shipmentDetailData;
        }

        private static ShipmentDetailData createDummyKargoShipmentDetailData() {
            ShipmentDetailData shipmentDetailData = new ShipmentDetailData();
            shipmentDetailData.setId(ID);
            shipmentDetailData.setAddress(ADDRESS);
            shipmentDetailData.setShipmentItemData(ShipmentChoicePresenter.DummyCreator.createDummyShipmentChoices());

            return shipmentDetailData;
        }

    }

}
