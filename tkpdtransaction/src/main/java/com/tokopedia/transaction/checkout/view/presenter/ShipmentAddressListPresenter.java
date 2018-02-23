package com.tokopedia.transaction.checkout.view.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.checkout.domain.model.ShipmentAddressModel;
import com.tokopedia.transaction.checkout.domain.usecase.GetAddressListUseCase;
import com.tokopedia.transaction.checkout.view.data.ShipmentRecipientModel;
import com.tokopedia.transaction.checkout.view.view.ISearchAddressListView;
import com.tokopedia.usecase.RequestParams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        extends CartMvpPresenter<ISearchAddressListView<List<ShipmentRecipientModel>>> {

    private static final String TAG = ShipmentAddressListPresenter.class.getSimpleName();

    private static final String PARAM_ORDER_BY = "order_by";
    private static final String PARAM_PAGE = "page";
    private static final String PARAM_QUERY = "query";

    private final GetAddressListUseCase mGetAddressListUseCase;
    private final PagingHandler mPagingHandler;

    @Inject
    public ShipmentAddressListPresenter(GetAddressListUseCase getAddressListUseCase,
                                        PagingHandler pagingHandler) {

        mGetAddressListUseCase = getAddressListUseCase;
        mPagingHandler = pagingHandler;
    }

    @Override
    public void attachView(ISearchAddressListView<List<ShipmentRecipientModel>> mvpView) {
        super.attachView(mvpView);
    }

    @Override
    protected void checkViewAttached() {
        super.checkViewAttached();
    }

    /**
     *
     * @param context
     * @param order
     * @param query
     */
    public void getAddressList(Context context, int order, String query) {
        mGetAddressListUseCase.execute(getPeopleAddressRequestParams(context, order, query),
                new Subscriber<List<ShipmentAddressModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Log.d(TAG, throwable.getMessage());
                    }

                    @Override
                    public void onNext(List<ShipmentAddressModel> shipmentAddressModels) {
                        Log.d(TAG, "size: " + shipmentAddressModels.size());
                    }
        });
    }

    /**
     *
     * @param context
     * @param order
     * @param query
     * @return
     */
    private RequestParams getPeopleAddressRequestParams(final Context context, final int order, final String query) {
        RequestParams requestParams = RequestParams.create();

        requestParams.putAll(new HashMap<String, Object>() {{
            putAll(generatePeopleAddressParams(context, order, query));
        }});

        return requestParams;
    }

    /**
     *
     * @param context
     * @param order
     * @param query
     * @return
     */
    private Map<String, String> generatePeopleAddressParams(Context context, final int order, final String query) {
        return AuthUtil.generateParams(context, new HashMap<String, String>() {{
            put(PARAM_ORDER_BY, String.valueOf(order));
            put(PARAM_QUERY, query);
            put(PARAM_PAGE, String.valueOf(mPagingHandler.getPage()));
        }});
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