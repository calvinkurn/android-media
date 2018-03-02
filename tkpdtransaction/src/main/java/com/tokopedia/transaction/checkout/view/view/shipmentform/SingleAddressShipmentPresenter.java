package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.transaction.checkout.view.base.CartMvpPresenter;

import java.util.List;

import javax.inject.Inject;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class SingleAddressShipmentPresenter
        extends CartMvpPresenter<ICartSingleAddressView> {

    private static final String TAG = SingleAddressShipmentPresenter.class.getSimpleName();

    @Inject
    public SingleAddressShipmentPresenter() {

    }

    @Override
    public void attachView(ICartSingleAddressView mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    public void getCartShipmentData(final List<Object> shipmentDataList) {
        getMvpView().show(shipmentDataList);
    }

}
