package com.tokopedia.seller.selling.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.seller.facade.FacadeShopTransaction;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingData;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Erry on 7/19/2016.
 */
public class SellingStatusTransactionImpl extends SellingStatusTransaction implements FacadeShopTransaction.GetStatusListener {

    private FacadeShopTransaction facade;
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    private static final String TAG = SellingStatusTransactionImpl.class.getSimpleName();
    private PagingHandler mPaging;
    private String search;
    private String filter;
    private String startDate;
    private String endDate;
    private Type type;

    public SellingStatusTransactionImpl(SellingStatusTransactionView view) {
        super(view);
        mPaging = new PagingHandler();
    }

    @Override
    public void loadMore(Context context) {
        view.setPullEnabled(false);
        if (mPaging.CheckNextPage()) {
            mPaging.nextPage();
            switch (type) {
                case STATUS:
                    facade.getStatusV4(mPaging, search, this);
                    break;
                case TRANSACTION:
                    facade.getTxListV4(mPaging, search, filter, startDate, endDate, this);
                    break;
            }
        }
    }

    @Override
    public void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context) {
        if (isVisibleToUser && context != null) {
        }
    }

    @Override
    public PagingHandler getPaging() {
        return mPaging;
    }

    @Override
    public String getMessageTAG() {
        return null;
    }

    @Override
    public String getMessageTAG(Class<?> className) {
        return null;
    }

    @Override
    public void initData(@NonNull Context context) {
        if (!isAfterRotate) {
            view.setupRecyclerView();
        }
    }

    @Override
    public void callNetworkTransaction(Context context, String search, String filter, String startDate, String endDate) {
        Log.d(TAG, "callNetworkTransaction");
        facade.setCompositeSubscription(_subscriptions);
        this.type = Type.TRANSACTION;
        this.search = search;
        this.filter = filter;
        this.startDate = startDate;
        this.endDate = endDate;
        mPaging.resetPage();
        facade.getTxListV4(mPaging, search, filter, startDate, endDate, this);
    }

    @Override
    public void callNetworkStatus(Context context, String search) {
        Log.d(TAG, "callNetworkStatus");
        facade.setCompositeSubscription(_subscriptions);
        this.type = Type.STATUS;
        this.search = search;
        mPaging.resetPage();
        facade.getStatusV4(mPaging, search, this);
    }

    @Override
    public void OnSuccess(List<SellingStatusTxModel> model, OrderShippingData Result) {
        CommonUtils.dumper(TAG + " : size " + model.size());
        //mPaging.setNewParameter(Result);
        if (mPaging.CheckNextPage()) {
            view.displayLoadMore(true);
        } else {
            view.displayLoadMore(false);
        }
        view.setPullEnabled(true);
        view.onCallStatusLoadMore(model);
    }

    @Override
    public void OnNoResult() {
        Log.d(TAG, "OnNoResult");
        mPaging.setHasNext(false);
        view.setPullEnabled(true);
        view.onNoResult();
    }

    @Override
    public void OnError() {
        Log.e(TAG, "OnError");
        view.setPullEnabled(true);
        view.onNetworkError(DownloadServiceConstant.INVALID_TYPE, "ERROR FETCH ORDER DATA");
    }

    @Override
    public void onNetworkTimeOut() {
        view.setPullEnabled(true);
        view.onNetworkError(DownloadServiceConstant.INVALID_TYPE, "ERROR NETWORK TIMEOUT");
    }

    @Override
    public void fetchArguments(Bundle argument) {

    }

    @Override
    public void fetchFromPreference(Context context) {

    }

    @Override
    public void getRotationData(Bundle argument) {

    }

    @Override
    public void saveDataBeforeRotation(Bundle argument) {

    }

    @Override
    public void initDataInstance(Context context) {
        if (!isAfterRotate) {
            view.initAdapter();
        }
        facade = FacadeShopTransaction.createInstance(context);
    }

    @Override
    public void subscribe() {
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);
    }

    @Override
    public void unSubscribe() {
        RxUtils.unsubscribeIfNotNull(_subscriptions);
    }

    public enum Type {
        STATUS, TRANSACTION
    }
}
