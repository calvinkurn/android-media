package com.tokopedia.transaction.checkout.view.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentDetailPresenter extends BaseDaggerPresenter<IShipmentDetailView>
        implements IShipmentDetailPresenter {

    private ShipmentDetailData shipmentDetailData;
    private CourierItemData selectedCourier;
    private ShipmentItemData selectedShipment;
    private List<CourierItemData> couriers = new ArrayList<>();

    //Temporary
    private Context context;

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

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
    public ShipmentItemData getSelectedShipment() {
        return selectedShipment;
    }

    @Override
    public void setSelectedShipment(ShipmentItemData selectedShipment) {
        this.selectedShipment = selectedShipment;
        setCourierList(selectedShipment.getCourierItemData());
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
        shipmentDetailData = DummyCreator.createDummyShipmentDetailData(context);
        if (shipmentDetailData != null) {
            getView().renderShipmentWithoutMap(shipmentDetailData);
        }
    }

    @Override
    public void loadFirstThreeCourier() {
        chooseSelectedCourier(selectedCourier);
        getView().showFirstThreeCouriers(couriers.subList(0, 3));
    }

    @Override
    public void loadAllCourier() {
        chooseSelectedCourier(selectedCourier);
        getView().showAllCouriers(couriers);
    }

    private void chooseSelectedCourier(CourierItemData currentCourier) {
        if (currentCourier != null) {
            for (int i = 0; i < couriers.size(); i++) {
                if (couriers.get(i).getId().equals(currentCourier.getId())) {
                    couriers.get(i).setSelected(true);
                } else {
                    couriers.get(i).setSelected(false);
                }
            }
        }
    }

    @Override
    public void getPinPointMapData() {
        if (shipmentDetailData != null) {
            getView().showPinPointChooserMap(shipmentDetailData);
        }
    }


    private static class DummyCreator {

        private static ShipmentDetailData createDummyShipmentDetailData(Context context) {
            String json = null;
            try {
                InputStream is = context.getAssets().open("shipment.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }

            if (!TextUtils.isEmpty(json)) {
                return new Gson().fromJson(json, ShipmentDetailData.class);
            }
            return null;
        }

    }

}
