package com.tokopedia.transaction.cart.presenter;

import com.tokopedia.core.geolocation.model.LocationPass;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.transaction.cart.interactor.CartDataInteractor;
import com.tokopedia.transaction.cart.interactor.ICartDataInteractor;
import com.tokopedia.transaction.cart.listener.IShipmentCartView;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentData;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentWrapper;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.calculateshipment.ShipmentPackage;
import com.tokopedia.transaction.cart.model.savelocation.LocationData;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationWrapper;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartData;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartWrapper;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 11/2/16.
 * modified by alvarisi
 */

public class ShipmentCartPresenter implements IShipmentCartPresenter {
    private final ICartDataInteractor interactor;
    private final IShipmentCartView view;

    public ShipmentCartPresenter(IShipmentCartView view) {
        this.view = view;
        this.interactor = new CartDataInteractor();
    }

    @Override
    public void processGeoCodeLocation(LocationPass locationPass) {
        Observable<String> observable = Observable.just(locationPass)
                .map(new Func1<LocationPass, String>() {
                    @Override
                    public String call(LocationPass locationPass) {
                        return GeoLocationUtils.reverseGeoCode(view.getActivity(),
                                locationPass.getLatitude(),
                                locationPass.getLongitude());
                    }
                });
        CompositeSubscription subscription = new CompositeSubscription();
        subscription.add(observable
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String location) {
                        view.renderGeocodeLocation(location);
                    }
                }));
    }

    @Override
    public void processCalculateShipment(CalculateShipmentWrapper wrapper) {
        if (this.view != null) {
            this.view.showLoading();
            this.interactor.calculateShipment(wrapper, new Subscriber<CalculateShipmentData>() {
                @Override
                public void onCompleted() {
                    view.dismisLoading();
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
                    view.renderCalculateShipment(data);
                }
            });
        }
    }

    @Override
    public void processEditShipmentCart(ShipmentCartWrapper wrapper) {
        if (this.view != null) {
            this.view.showLoading();
            this.interactor.editShipmentCart(wrapper, new Subscriber<ShipmentCartData>() {
                @Override
                public void onCompleted() {
                }

                @Override
                public void onError(Throwable e) {
                    view.renderErrorEditShipment(e.getMessage());
                }

                @Override
                public void onNext(ShipmentCartData shipmentCartData) {
                    view.dismisLoading();
                    if (shipmentCartData.getStatus().equalsIgnoreCase("1")) {
                        view.navigateToCart(shipmentCartData.getMessage());
                    } else {
                        view.renderErrorEditShipment(shipmentCartData.getMessage());
                    }
                }
            });
        }
    }

    @Override
    public void processSaveLocationShipment(SaveLocationWrapper wrapper) {

    }
}
