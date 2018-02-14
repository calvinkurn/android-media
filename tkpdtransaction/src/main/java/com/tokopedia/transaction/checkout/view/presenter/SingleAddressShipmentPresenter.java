package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.transaction.checkout.view.data.CartSingleAddressData;
import com.tokopedia.transaction.checkout.view.data.factory.CartSingleAddressDataFactory;
import com.tokopedia.transaction.checkout.view.view.ICartSingleAddressView;

import rx.Observer;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public class SingleAddressShipmentPresenter
        extends CartMvpPresenter<ICartSingleAddressView<CartSingleAddressData>> {

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

    public void getCartSingleAddressItemView(CartSingleAddressData cartSingleAddressData) {
        // TODO remove this, and invoke use case
        getMvpView().show(cartSingleAddressData);
    }



    private final class CartSingleAddressObserver implements Observer<CartSingleAddressData> {

        @Override
        public void onNext(CartSingleAddressData cartSingleAddressData) {
            getMvpView().show(cartSingleAddressData);
        }

        @Override
        public void onError(Throwable e) {
            getMvpView().showError();
        }

        @Override
        public void onCompleted() {

        }

    }

}
