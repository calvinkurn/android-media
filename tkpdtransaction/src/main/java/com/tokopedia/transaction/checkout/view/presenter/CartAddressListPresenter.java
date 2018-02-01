package com.tokopedia.transaction.checkout.view.presenter;

import com.tokopedia.transaction.checkout.view.data.ShippingRecipientModel;
import com.tokopedia.transaction.checkout.view.data.factory.ShippingRecipientModelFactory;
import com.tokopedia.transaction.checkout.view.view.ISearchAddressListView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
        filter(fetchAddressList(), keyword);
    }

    public void resetSearch() {

    }

    public void getAddressList() {
        // TODO remove this, and invoke use case
        getMvpView().showList(fetchAddressList());
    }

    private List<ShippingRecipientModel> fetchAddressList() {
        return ShippingRecipientModelFactory.getDummyShippingRecipientModelList();
    }

    private void filter(List<ShippingRecipientModel> addressList, final String keyword) {
        Observable.from(addressList)
                .debounce(500, TimeUnit.MILLISECONDS)
                .filter(new Func1<ShippingRecipientModel, Boolean>() {
                    @Override
                    public Boolean call(ShippingRecipientModel shippingRecipientMode) {
                        boolean isNameContainsKeyword = shippingRecipientMode.getRecipientName().contains(keyword);
                        boolean isAddressContainKeyword = shippingRecipientMode.getRecipientAddress().contains(keyword);

                        boolean result = isAddressContainKeyword || isNameContainsKeyword;
                        return result;
                    }
                })
                .toList()
                .observeOn(Schedulers.computation())
                .subscribe(new AddressListObserver());
        // TODO make sure to put proper scheduler
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
