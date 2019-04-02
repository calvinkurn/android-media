package com.tokopedia.seller.selling.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.tokopedia.core2.R;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.ValidationTextUtil;
import com.tokopedia.seller.facade.FacadeShopTransaction;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingData;
import com.tokopedia.seller.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.seller.selling.view.activity.SellingDetailActivity;
import com.tokopedia.seller.selling.view.fragment.FragmentSellingNewOrder;
import com.tokopedia.transaction.common.TransactionRouter;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Toped10 on 7/18/2016.
 */
public class NewOrderImpl extends NewOrder {

    private Context context;

    private boolean isLoading;
    private boolean isRefresh;

    private Model modelNewOrder = new Model();
    public List<OrderShippingList> listDatas = new ArrayList<>();

    private FacadeShopTransaction facade;

    public NewOrderImpl(NewOrderView view) {
        super(view);
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
    public void checkValidationToSendGoogleAnalytic(boolean isVisibleToUser, Context context) {
        if (isVisibleToUser && context != null) {
        }
    }

    @Override
    public void getOrderList(boolean isVisibleToUser) {
        if (isVisibleToUser && isDataEmpty() && !isLoading) {
            getOrderList();
        }
    }

    public void getOrderList() {
        view.disableFilter();
        isLoading = true;
        requestGetNewOrder();
    }

    @Override
    public void onQueryChange(String newText) {
        if (newText.length() == 0)
            doRefresh();
    }

    @Override
    public void onQuerySubmit(String query) {
        if (!isLoading && view.getUserVisible()) {
            if (ValidationTextUtil.isValidSalesQuery(query)) {
                view.hideFilterView();
                onRefreshOrder();
            } else {
                showToastMessage(context.getString(R.string.keyword_min_3_char));
            }
        }
    }

    @Override
    public void onDeadlineSelected() {
        if (!isLoading && view.getUserVisible()) {
            view.hideFilterView();
            onRefreshOrder();
        }
    }


    @Override
    public void onScrollList(boolean isLastItemVisible) {
        if (!isLoading && isLastItemVisible && view.getPaging().CheckNextPage()) {
            onLoadNextPage();
        }
    }

    @Override
    public void onRefreshView() {
        onRefreshOrder();
    }


    @Override
    public void moveToDetail(int position) {
        if (listDatas != null && position >= 0 && listDatas.get(position) != null) {
            Intent intent = ((TransactionRouter) MainApplication.getAppContext())
                    .goToOrderDetail(
                            context,
                            listDatas.get(position)
                                    .getOrderDetail()
                                    .getDetailOrderId()
                                    .toString());
            view.moveToDetailResult(intent, FragmentSellingNewOrder.PROCESS_ORDER);
        }
    }

    private void requestGetNewOrder() {
        facade.setCompositeSubscription(compositeSubscription);
        facade.getNewOrder(view.getPaging(), view.getQuery(), view.getDeadlineSelectionPos(), OnRequestNewOrder());
    }

    private void clearData() {
        listDatas.clear();
        view.notifyDataSetChanged(listDatas);
    }

    @Override
    public void finishConnection() {
        view.finishRefresh();
        view.removeRetry();
        isLoading = false;
        view.removeLoading();
        view.removeEmpty();
        view.enableFilter();
    }

    private boolean isDataEmpty() {
        try {
            return (view.getPaging().getPage() == 1 && !isLoading && listDatas.size() == 0);
        } catch (Exception e) {
            return false;
        }

    }

    private boolean isAllowLoading() {
        return (isDataEmpty() || isLoading);
    }

    private void addLoading() {
        view.addLoadingFooter();
    }

    private void initData() {
        if (view.getUserVisible() && isDataEmpty() && !isLoading)
            getOrderList();
    }

    private void onRefreshOrder() {
        if (!isLoading) {
            view.removeRetry();
            doRefresh();
        }
    }

    private void doRefresh() {
        view.getPaging().resetPage();
        if (!view.getRefreshing()) {
            view.addLoadingFooter();
            clearData();
        }
        getOrderList();
    }

    private void showToastMessage(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    private void onLoadNextPage() {
        view.addLoadingFooter();
        view.getPaging().nextPage();
        getOrderList();
    }

    private FacadeShopTransaction.GetNewOrderListener OnRequestNewOrder() {
        return new FacadeShopTransaction.GetNewOrderListener() {
            @Override
            public void OnSuccess(Model model, OrderShippingData Result) {
                if (view.getPaging().getPage() == 1) {
                    AppWidgetUtil.sendBroadcastToAppWidget(context);
                    clearData();
                }
                view.getPaging().setNewParameter(Result.getPaging());
                finishConnection();
                modelNewOrder = model;
                listDatas.addAll(modelNewOrder.DataList);
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
            public void OnNoNextPage() {
                view.getPaging().setHasNext(false);
            }

            @Override
            public void OnError() {
                finishConnection();
                if (listDatas.size() == 0) {
                    view.hideFab();
                    view.addRetry();
                } else {
                    NetworkErrorHelper.showSnackbar((Activity) context);
                }

            }

            @Override
            public void onNetworkTimeOut() {
//                CommonUtils.dumper("NISIETAGCONNECTION : TIMEOUT EMPTY : " + isDataEmpty() + "IS REFRESH :" + refresh.isRefreshing());
//                finishTimeout();
//                if (isDataEmpty()) {
//                    refresh.setPullEnabled(false);
//                    viewHolder.list.addRetry();
//                } else {
//                    try {
//                        CommonUtils.UniversalToast(getActivity(), getActivity().getString(R.string.msg_connection_timeout_toast));
//                    } catch (NullPointerException e) {
//                    }
//                    refresh.setPullEnabled(true);
//                    if (!refresh.isRefreshing()) {
//                        viewHolder.list.addRetry();
//                    }
//                }
            }
        };
    }

    public static class Model {
        public List<OrderShippingList> DataList;
        public String Permission;
    }
}
