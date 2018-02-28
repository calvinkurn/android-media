package com.tokopedia.transaction.checkout.view.view.shipmentform;

import com.tokopedia.transaction.checkout.domain.datamodel.cartsingleshipment.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.base.CartMvpPresenter;

import javax.inject.Inject;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class SingleAddressShipmentPresenter
        extends CartMvpPresenter<ICartSingleAddressView<CartSingleAddressData>> {

    private static final String TAG = SingleAddressShipmentPresenter.class.getSimpleName();

    @Inject
    public SingleAddressShipmentPresenter() {

    }

    @Override
    public void attachView(ICartSingleAddressView<CartSingleAddressData> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    /**
     *
     * @param context
     * @param cartSingleAddressData
     */
    public void getCartShipmentData(final CartSingleAddressData cartSingleAddressData) {
        getMvpView().show(cartSingleAddressData);
    }

}
