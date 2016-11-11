package com.tokopedia.transaction.cart.presenter;

import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.transaction.cart.interactor.CartDataInteractor;
import com.tokopedia.transaction.cart.interactor.ICartDataInteractor;
import com.tokopedia.transaction.cart.listener.IShipmentCartView;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentData;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentWrapper;
import com.tokopedia.transaction.cart.model.savelocation.LocationData;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationWrapper;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartWrapper;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import rx.Subscriber;

/**
 * @author anggaprasetiyo on 11/2/16.
 */

public class ShipmentCartPresenter implements IShipmentCartPresenter {
    private final ICartDataInteractor interactor;
    private final IShipmentCartView view;

    public ShipmentCartPresenter(IShipmentCartView view) {
        this.view = view;
        this.interactor = new CartDataInteractor();
    }

    @Override
    public void goToChangeAddress() {
        if (view != null) {

        }
    }

    @Override
    public void goToAddAddress() {

    }

    @Override
    public void goToGeolocation(LocationData data) {

    }

    @Override
    public void backToCart() {

    }

    @Override
    public void processCalculateShipment(CalculateShipmentWrapper wrapper) {
        if (this.view != null) {
            this.view.showLoading();
            this.interactor.calculateShipment(wrapper, new Subscriber<CalculateShipmentData>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                    e.printStackTrace();
                    if (e instanceof UnknownHostException) {
                        view.renderErrorCalculateShipment(
                                ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                        );
                    } else if (e instanceof SocketTimeoutException) {
                        view.renderErrorCalculateShipment(
                                ErrorNetMessage.MESSAGE_ERROR_TIMEOUT
                        );
                    } else {
                        view.renderErrorCalculateShipment(
                                ErrorNetMessage.MESSAGE_ERROR_DEFAULT
                        );
                    }
                }

                @Override
                public void onNext(CalculateShipmentData data) {

                }
            });
        }
    }

    @Override
    public void processEditShipmentCart(ShipmentCartWrapper wrapper) {

    }

    @Override
    public void processSaveLocationShipment(SaveLocationWrapper wrapper) {

    }
}
