package com.tokopedia.seller.selling.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.tokopedia.core2.R;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.ValidationTextUtil;
import com.tokopedia.seller.facade.FacadeShopTransaction;
import com.tokopedia.seller.selling.model.SellingStatusTxModel;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingData;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Erry on 7/19/2016.
 */
public class SellingStatusTransactionImpl extends SellingStatusTransaction implements FacadeShopTransaction.GetStatusListener {

    private FacadeShopTransaction facade;
    private CompositeSubscription _subscriptions = new CompositeSubscription();
    private static final String TAG = SellingStatusTransactionImpl.class.getSimpleName();
    private Type type;

    private boolean isLoading;

    public List<SellingStatusTxModel> listDatas = new ArrayList<>();
    private Context context;

    public SellingStatusTransactionImpl(SellingStatusTransactionView view, Type type) {
        super(view);
        this.type = type;
    }

    @Override
    public void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context) {
        if (isVisibleToUser && context != null) {
        }
    }

    @Override
    public void getStatusTransactionList(boolean isVisibleToUser, Type type) {
        this.type = type;
        if (isVisibleToUser && isDataEmpty() && !isLoading) {
            addLoading();
            switch (type) {
                case STATUS:
                    getStatusList();
                    break;
                case TRANSACTION:
                    getTransactionList();
                    break;
            }
        }
    }

    private void getStatusList() {

        isLoading = true;
        requestGetStatusList();
    }

    private void getTransactionList() {
        isLoading = true;
        requestGetTransactionList();
    }

    private void requestGetTransactionList() {
        facade.setCompositeSubscription(_subscriptions);
        facade.getTxListV4(view.getPaging(), view.getQuery(), view.getFilter(), view.getStartDate(), view.getEndDate(), this);
    }

    public void requestGetStatusList() {
        facade.setCompositeSubscription(_subscriptions);
        facade.getStatusV4(view.getPaging(), view.getQuery(), this);
    }

    private boolean isDataEmpty() {
        try {
            return (view.getPaging().getPage() == 1 && !isLoading && listDatas.size() == 0);
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public void onQuerySubmit(String query) {
        if (!isLoading && view.getUserVisible()) {
            if (ValidationTextUtil.isValidSalesQuery(query)) {
                view.hideFilterView();
                onRefreshStatusTransaction();
            } else {
                showToastMessage(context.getString(R.string.keyword_min_3_char));
            }
        }
    }

    @Override
    public void refreshOnFilter() {
        if(!isLoading && view.getUserVisible()) {
            onRefreshStatusTransaction();
            view.hideFilterView();
        }
    }

    private void showToastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void onRefreshStatusTransaction() {
        if (!isLoading) {
            view.removeRetry();
            doRefresh();
        }
    }

    @Override
    public void onQueryChange(String newText) {
        if (newText.length() == 0)
            doRefresh();
    }

    private void doRefresh() {
        view.getPaging().resetPage();
        if (!view.getRefreshing()) {
            view.addLoadingFooter();
            clearData();
        }
        switch (type){
            case STATUS:
                getStatusList();
                break;
            case TRANSACTION:
                getTransactionList();
                break;
        }
    }

    private void clearData() {
        listDatas.clear();
        view.notifyDataSetChanged(listDatas);
    }

    @Override
    public void onRefreshView() {
        onRefreshStatusTransaction();
    }

    @Override
    public void onScrollList(boolean isLastItemVisible) {
        if (!isLoading && isLastItemVisible && view.getPaging().CheckNextPage()) {
            onLoadNextPage();
        }
    }

    private void onLoadNextPage() {
        view.addLoadingFooter();
        view.getPaging().nextPage();
        switch (type) {
            case STATUS:
                getStatusList();
                break;
            case TRANSACTION:
                getTransactionList();
                break;
        }
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
        view.initListener();
        if (!isAfterRotate) {
            if (isAllowLoading()) {
                addLoading();
            }
            initData();
        }
    }

    private void initData() {
        if (view.getUserVisible() && isDataEmpty() && !isLoading) {
            switch (type) {
                case STATUS:
                    getStatusList();
                    break;
                case TRANSACTION:
                    getTransactionList();
                    break;
            }
        }
    }

    private boolean isAllowLoading() {
        return (isDataEmpty() || isLoading);
    }

    private void addLoading() {
        view.addLoadingFooter();
    }

    @Override
    public void OnSuccess(List<SellingStatusTxModel> model, OrderShippingData Result) {
        if (view.getPaging().getPage() == 1)
            clearData();
        view.getPaging().setNewParameter(Result.getPaging());
        finishConnection();
        listDatas.addAll(model);
        view.notifyDataSetChanged(listDatas);
        view.showFab();
    }

    @Override
    public void OnNoResult() {
        finishConnection();
        if (view.getPaging().getPage() == 1) {
            clearData();
            view.addEmptyView();
        }
        view.getPaging().setHasNext(false);
        view.showFab();
        view.removeRetry();
    }

    @Override
    public void OnError() {
        finishConnection();
        if (listDatas.size() == 0) {
            view.addRetry();
            view.hideFab();
        } else {
            NetworkErrorHelper.showSnackbar((Activity) context);
        }
    }

    @Override
    public void onNetworkTimeOut() {
        finishConnection();
        if (listDatas.size() == 0) {
            view.addRetry();
            view.hideFab();
        } else {
            NetworkErrorHelper.showSnackbar((Activity) context);
        }
    }

    @Override
    public void finishConnection() {
        view.finishRefresh();
        view.removeRetry();
        isLoading = false;
        view.removeLoading();
        view.removeEmpty();
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
        this.context = context;
        facade = FacadeShopTransaction.createInstance(context);
        view.initHandlerAndAdapter();
        checkValidationToSendGoogleAnalytic(view.getUserVisible(), context);
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
