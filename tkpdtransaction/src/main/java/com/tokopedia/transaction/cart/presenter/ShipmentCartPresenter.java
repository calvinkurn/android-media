package com.tokopedia.transaction.cart.presenter;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.geolocation.model.autocomplete.LocationPass;
import com.tokopedia.core.geolocation.utils.GeoLocationUtils;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.transaction.cart.interactor.CartDataInteractor;
import com.tokopedia.transaction.cart.interactor.ICartDataInteractor;
import com.tokopedia.transaction.cart.interactor.exception.WrongEditCartException;
import com.tokopedia.transaction.cart.listener.IShipmentCartView;
import com.tokopedia.transaction.cart.model.calculateshipment.CalculateShipmentWrapper;
import com.tokopedia.transaction.cart.model.calculateshipment.Shipment;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationWrapper;
import com.tokopedia.transaction.cart.model.shipmentcart.EditShipmentCart;
import com.tokopedia.transaction.cart.model.shipmentcart.ShipmentCartWrapper;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 11/2/16.
 *         modified by alvarisi
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
                .subscribe(new GeocodeLocationSubscriber()));
    }

    @Override
    public void processCalculateShipment(CalculateShipmentWrapper wrapper) {
        if (this.view != null) {
            if (!this.view.isLoading()) {
                this.view.showLoading();
            }
            this.interactor.calculateShipment(
                    AuthUtil.generateParamsNetwork(
                            MainApplication.getAppContext(), wrapper.getParams()
                    ),
                    new CalculateShipmentSubscriber()
            );
        }
    }

    @Override
    public void processEditShipmentCart(ShipmentCartWrapper wrapper) {
        if (this.view != null) {
            this.view.showLoading();
            this.interactor.editShipmentCart(AuthUtil.generateParamsNetwork(view.getActivity(),
                    wrapper.getParams()),
                    new EditShipmentCartSubscriber()
            );
        }
    }

    @Override
    public void processSaveLocationShipment(SaveLocationWrapper wrapper) {
        if (this.view != null) {
            this.view.showLoading();
            this.interactor.editLocationShipment(
                    AuthUtil.generateParamsNetwork(
                            MainApplication.getAppContext(), wrapper.getParams()
                    ),
                    new SaveLocationShipmentSubscriber()
            );
        }
    }

    private final class EditShipmentCartSubscriber extends Subscriber<EditShipmentCart> {
        @Override
        public void onCompleted() {
            view.dismisLoading();
        }

        @Override
        public void onError(Throwable e) {
            view.dismisLoading();
            if (e instanceof UnknownHostException) {
                view.renderErrorEditShipment(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_SHORT);
            } else if (e instanceof SocketTimeoutException) {
                view.renderErrorEditShipment(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT_SHORT);
            } else if (e instanceof WrongEditCartException) {
                view.renderEditShipmentErrorSnackbar(e.getMessage());
            } else {
                view.renderErrorEditShipment(e.getMessage());
            }
        }

        @Override
        public void onNext(EditShipmentCart editShipmentCart) {
            if (editShipmentCart.getStatus().equalsIgnoreCase("1")) {
                view.navigateToCart(editShipmentCart.getMessage());
            } else {
                view.renderErrorEditShipment(editShipmentCart.getMessage());
            }
        }
    }

    private final class GeocodeLocationSubscriber extends Subscriber<String> {
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(String location) {
            view.renderGeocodeLocation(location);
        }
    }

    private final class CalculateShipmentSubscriber extends Subscriber<List<Shipment>> {
        @Override
        public void onCompleted() {
            view.dismisLoading();
        }

        @Override
        public void onError(Throwable e) {
            view.dismisLoading();
            e.printStackTrace();
            if (e instanceof UnknownHostException) {
                view.renderErrorCalculateShipment(
                        ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_SHORT
                );
            } else if (e instanceof SocketTimeoutException) {
                view.renderErrorCalculateShipment(
                        ErrorNetMessage.MESSAGE_ERROR_TIMEOUT_SHORT
                );
            } else {
                view.renderErrorCalculateShipment(
                        ErrorNetMessage.MESSAGE_ERROR_DEFAULT_SHORT
                );
            }
        }

        @Override
        public void onNext(List<Shipment> shipments) {
            view.renderCalculateShipment(shipments);
        }
    }

    private final class SaveLocationShipmentSubscriber extends Subscriber<SaveLocationData> {
        @Override
        public void onCompleted() {
        }

        @Override
        public void onError(Throwable e) {
            view.dismisLoading();
            view.renderErrorEditLocationShipment(e.getMessage());
        }

        @Override
        public void onNext(SaveLocationData saveLocationData) {
            if (saveLocationData.getStatus().equalsIgnoreCase("1")) {
                view.renderEditLocationShipment(saveLocationData.getMessage());
            } else {
                view.renderErrorEditLocationShipment(saveLocationData.getMessage());
            }
        }
    }

    @Override
    public void destroy() {
        this.interactor.unSubscribeObservable();
    }
}
