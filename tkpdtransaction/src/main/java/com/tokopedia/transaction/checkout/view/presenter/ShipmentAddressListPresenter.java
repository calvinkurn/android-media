package com.tokopedia.transaction.checkout.view.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.data.factory.ShipmentRecipientModelFactory;
import com.tokopedia.transaction.checkout.view.view.ISearchAddressListView;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Aghny A. Putra on 26/01/18
 */
public class ShipmentAddressListPresenter
        extends CartMvpPresenter<ISearchAddressListView<List<ShipmentRecipientModel>>> {

    private static final String TAG = ShipmentAddressListPresenter.class.getSimpleName();

    public ShipmentAddressListPresenter() {

    }

    @Override
    public void attachView(ISearchAddressListView<List<ShipmentRecipientModel>> mvpView) {
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
        filter(fetchAddressList(), "");
    }

    public void getAddressList() {
        getMvpView().showList(fetchAddressList());
    }

    private List<ShipmentRecipientModel> fetchAddressList() {
        // TODO remove this, and invoke use case
        return ShipmentRecipientModelFactory.getDummyShipmentRecipientModelList();
    }

    private void filter(List<ShipmentRecipientModel> addressList, final String keyword) {
        Observable.from(addressList)
                .filter(new Func1<ShipmentRecipientModel, Boolean>() {
                    @Override
                    public Boolean call(ShipmentRecipientModel shippingRecipientMode) {
                        if (TextUtils.isEmpty(keyword)) return true;

                        String lowCaseKeyword = keyword.toLowerCase();

                        boolean isNameContainsKeyword = shippingRecipientMode.getRecipientName().toLowerCase()
                                .contains(lowCaseKeyword);
                        boolean isAddressContainsKeyword = shippingRecipientMode.getRecipientAddress().toLowerCase()
                                .contains(lowCaseKeyword);
                        boolean isIdentifierContainsKeyword = shippingRecipientMode.getAddressIdentifier().toLowerCase()
                                .contains(lowCaseKeyword);

                        return isNameContainsKeyword || isAddressContainsKeyword
                                || isIdentifierContainsKeyword;
                    }
                })
                .toList()
                .observeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new AddressListObserver());
        // TODO make sure to put proper scheduler
    }

    private final class AddressListObserver implements Observer<List<ShipmentRecipientModel>> {

        @Override
        public void onNext(List<ShipmentRecipientModel> addressList) {
            Log.d(TAG, "size: " + addressList.size());
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