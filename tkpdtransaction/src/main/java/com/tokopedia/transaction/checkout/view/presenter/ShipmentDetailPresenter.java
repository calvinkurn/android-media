package com.tokopedia.transaction.checkout.view.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.GetRatesUseCase;
import com.tokopedia.transaction.checkout.domain.response.rates.RatesResponse;
import com.tokopedia.transaction.checkout.view.data.CourierItemData;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.transaction.checkout.view.data.ShipmentItemData;
import com.tokopedia.transaction.checkout.view.view.IShipmentDetailView;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by Irfan Khoirul on 24/01/18.
 */

public class ShipmentDetailPresenter extends BaseDaggerPresenter<IShipmentDetailView>
        implements IShipmentDetailPresenter {

    private ShipmentDetailData shipmentDetailData;
    private CourierItemData selectedCourier;
    private ShipmentItemData selectedShipment;
    private List<CourierItemData> couriers = new ArrayList<>();
    private GetRatesUseCase getRatesUseCase;

    @Inject
    public ShipmentDetailPresenter(GetRatesUseCase getRatesUseCase) {
        this.getRatesUseCase = getRatesUseCase;
    }

    @Override
    public void attachView(IShipmentDetailView view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        getRatesUseCase.unsubscribe();
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
        this.couriers.clear();
        this.couriers.addAll(couriers);
        loadAllCourier();
    }

    @Override
    public void updatePinPoint(LocationPass locationPass) {
        shipmentDetailData.setDestinationLatitude(Double.parseDouble(locationPass.getLatitude()));
        shipmentDetailData.setDestinationLongitude(Double.parseDouble(locationPass.getLongitude()));
        shipmentDetailData.setAddress(locationPass.getGeneratedAddress());
        getView().showPinPointMap(shipmentDetailData);
    }

    @Override
    public void loadShipmentData() {
        getView().showLoading();
        getRatesUseCase.setShipmentDetailData(new ShipmentDetailData());
        getRatesUseCase.execute(getRatesUseCase.getParams(), new Subscriber<ShipmentDetailData>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                if (isViewAttached()) {
                    getView().hideLoading();
                    String message;
                    if (e instanceof UnknownHostException || e instanceof ConnectException ||
                            e instanceof SocketTimeoutException) {
                        message = getView().getActivity().getResources().getString(R.string.msg_no_connection);
                    } else if (e instanceof UnProcessableHttpException) {
                        message = TextUtils.isEmpty(e.getMessage()) ?
                                getView().getActivity().getResources().getString(R.string.msg_no_connection) :
                                e.getMessage();
                    } else {
                        message = getView().getActivity().getResources().getString(R.string.default_request_error_unknown);
                    }
                    getView().showNoConnection(message);
                }
            }

            @Override
            public void onNext(ShipmentDetailData shipmentDetailData) {
                if (isViewAttached()) {
                    ShipmentDetailPresenter.this.shipmentDetailData = shipmentDetailData;
                    getView().hideLoading();
                    getView().renderFirstLoadedRatesData(shipmentDetailData);
                }
            }
        });

    }

    @Override
    public void loadAllCourier() {
        chooseSelectedCourier(selectedCourier);
        getView().showAllCouriers();
    }

    @Override
    public List<CourierItemData> getCouriers() {
        return couriers;
    }

    private void chooseSelectedCourier(CourierItemData currentCourier) {
        if (currentCourier != null) {
            for (int i = 0; i < couriers.size(); i++) {
                if (couriers.get(i).getShipperProductId() == currentCourier.getShipperProductId()) {
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

}
