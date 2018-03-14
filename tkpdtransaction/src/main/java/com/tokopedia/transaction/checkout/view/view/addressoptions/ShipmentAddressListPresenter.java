package com.tokopedia.transaction.checkout.view.view.addressoptions;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.network.exception.model.UnProcessableHttpException;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.checkout.domain.datamodel.addressoptions.RecipientAddressModel;
import com.tokopedia.transaction.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.transaction.checkout.view.base.CartMvpPresenter;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Aghny A. Putra on 26/01/18
 */

public class ShipmentAddressListPresenter
        extends CartMvpPresenter<ISearchAddressListView<List<RecipientAddressModel>>> {

    private static final String TAG = ShipmentAddressListPresenter.class.getSimpleName();

    private static final String DEFAULT_KEYWORD = "";

    private final GetPeopleAddressUseCase mGetPeopleAddressUseCase;
    private final PagingHandler mPagingHandler;

    @Inject
    public ShipmentAddressListPresenter(GetPeopleAddressUseCase getPeopleAddressUseCase,
                                        PagingHandler pagingHandler) {
        mGetPeopleAddressUseCase = getPeopleAddressUseCase;
        mPagingHandler = pagingHandler;
    }

    @Override
    public void attachView(ISearchAddressListView<List<RecipientAddressModel>> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    /**
     * @param context
     * @param order
     */
    public void resetAddressList(Context context, int order) {
        getAddressList(context, order, DEFAULT_KEYWORD);
    }

    /**
     * @param context
     * @param order
     * @param query
     */
    public void getAddressList(Context context, int order, String query) {
        getMvpView().showLoading();
        mGetPeopleAddressUseCase.execute(mGetPeopleAddressUseCase
                        .getRequestParams(context, order, query, mPagingHandler.getPage()),
                new Subscriber<List<RecipientAddressModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.d(TAG, throwable.getMessage());
                        throwable.printStackTrace();
                        if (isViewAttached()) {
                            getMvpView().hideLoading();
                            String message;
                            if (throwable instanceof UnknownHostException ||
                                    throwable instanceof ConnectException ||
                                    throwable instanceof SocketTimeoutException) {
                                message = getMvpView().getActivity().getResources().getString(
                                        R.string.msg_no_connection);
                            } else if (throwable instanceof UnProcessableHttpException) {
                                message = TextUtils.isEmpty(throwable.getMessage()) ?
                                        getMvpView().getActivity().getResources().getString(
                                                R.string.msg_no_connection) :
                                        throwable.getMessage();
                            } else {
                                message = getMvpView().getActivity().getResources().getString(
                                        R.string.default_request_error_unknown);
                            }
                            getMvpView().showError(message);
                        }
                    }

                    @Override
                    public void onNext(List<RecipientAddressModel> shipmentAddressModels) {
                        boolean viewIsAttached = isViewAttached();
                        if (viewIsAttached) {
                            getMvpView().hideLoading();
                            if (shipmentAddressModels.isEmpty()) {
                                getMvpView().showListEmpty();
                            } else {
                                getMvpView().showList(shipmentAddressModels);
                            }
                        }

                        Log.d(TAG, "Size: " + shipmentAddressModels.size());
                    }
                });
    }

    private void filter(List<RecipientAddressModel> addressList, final String keyword) {
        Observable.from(addressList)
                .filter(new Func1<RecipientAddressModel, Boolean>() {
                    @Override
                    public Boolean call(RecipientAddressModel shippingRecipientMode) {
                        if (TextUtils.isEmpty(keyword)) {
                            return true;
                        }

                        String lowCaseKeyword = keyword.toLowerCase();

                        boolean isNameContainsKeyword = shippingRecipientMode
                                .getRecipientName()
                                .toLowerCase()
                                .contains(lowCaseKeyword);

                        boolean isProvinceContainsKeyword = shippingRecipientMode
                                .getAddressProvinceName()
                                .toLowerCase()
                                .contains(lowCaseKeyword);

                        boolean isCityContainsKeyword = shippingRecipientMode
                                .getAddressCityName()
                                .toLowerCase()
                                .contains(lowCaseKeyword);

                        boolean isDistrictContainsKeyword = shippingRecipientMode
                                .getDestinationDistrictName()
                                .toLowerCase()
                                .contains(lowCaseKeyword);

                        boolean isStreetContainsKeyword = shippingRecipientMode
                                .getAddressStreet()
                                .toLowerCase()
                                .contains(lowCaseKeyword);

                        boolean isIdentifierContainsKeyword = shippingRecipientMode
                                .getAddressName()
                                .toLowerCase()
                                .contains(lowCaseKeyword);

                        return isNameContainsKeyword
                                || isProvinceContainsKeyword
                                || isCityContainsKeyword
                                || isDistrictContainsKeyword
                                || isStreetContainsKeyword
                                || isIdentifierContainsKeyword;
                    }
                })
                .toList()
                .observeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new AddressListObserver());
        // TODO make sure to put proper scheduler
    }

    private final class AddressListObserver implements Observer<List<RecipientAddressModel>> {

        @Override
        public void onNext(List<RecipientAddressModel> addressList) {
            if (addressList.isEmpty()) {
                getMvpView().showListEmpty();
            } else {
                getMvpView().showList(addressList);
            }
        }

        @Override
        public void onError(Throwable e) {
            getMvpView().showError("Terjadi kesalahan");
        }

        @Override
        public void onCompleted() {

        }

    }

}