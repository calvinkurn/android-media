package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.transaction.checkout.view.data.ShippingRecipientModel;
import com.tokopedia.transaction.checkout.view.data.factory.ShippingRecipientModelFactory;
import com.tokopedia.transaction.checkout.view.view.ISearchAddressListView;

import java.util.List;

import rx.Observer;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public class CartAddressListPresenter
        extends CartMvpPresenter<ISearchAddressListView<List<ShippingRecipientModel>>> {

    public CartAddressListPresenter() {

    }

    @Override
    public void attachView(ISearchAddressListView<List<ShippingRecipientModel>> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    public void initSearch(String keyword) {
        // TODO execute search use case
    }

    public void resetSearch() {

    }

    public void getAddressList() {
        // TODO remove this, and invoke use case
        getMvpView().showList(ShippingRecipientModelFactory.getDummyShippingRecipientModelList());
    }

    private final class AddressListObserver implements Observer<List<ShippingRecipientModel>> {

        @Override
        public void onNext(List<ShippingRecipientModel> addressList) {
            if (addressList.isEmpty()) {
                getMvpView().showListEmpty();
            } else {
                getMvpView().showList(addressList);
            }
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
