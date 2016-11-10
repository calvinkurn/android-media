package com.tokopedia.core.selling.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.tokopedia.seller.facade.FacadeShopTransaction;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.selling.view.activity.SellingDetailActivity;
import com.tokopedia.core.selling.view.fragment.FragmentSellingNewOrder;
import com.tokopedia.core.selling.model.orderShipping.OrderShippingData;
import com.tokopedia.core.selling.model.orderShipping.OrderShippingList;
import com.tokopedia.core.util.ValidationTextUtil;

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
    public List<OrderShippingList> ListDatas = new ArrayList<>();

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
        view.initView();
        if (isAllowLoading()) {
            addLoading();
        }
        view.initListener();
        initData();
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
        view.setRefreshPullEnable(false);
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
                showToastMessage("Keyword terlalu pendek, minimal 3 karakter");
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
        Intent intent = new Intent(context, SellingDetailActivity.class);
        intent.putExtra(SellingDetailActivity.DATA_EXTRA, Parcels.wrap(ListDatas.get(position)));
        intent.putExtra(SellingDetailActivity.DATA_EXTRA2, modelNewOrder.Permission);
        intent.putExtra(SellingDetailActivity.TYPE_EXTRA, SellingDetailActivity.Type.NEW_ORDER);
//        context.startActivity(intent);
        view.moveToDetailResult(intent, FragmentSellingNewOrder.PROCESS_ORDER);
    }

    private void requestGetNewOrder() {
        facade.setCompositeSubscription(compositeSubscription);
        facade.getNewOrder(view.getPaging(), view.getQuery(), view.getDeadlineSelectionPos(), OnRequestNewOrder());
    }

    private void clearData() {
        ListDatas.clear();
        view.notifyDataSetChanged(ListDatas);
    }

    private void finishConnection() {
        view.finishRefresh();
        view.removeRetry();
        isLoading = false;
        view.removeLoading();
        view.enableFilter();
    }

    private boolean isDataEmpty() {
        try {
            return (view.getPaging().getPage() == 1 && !isLoading && ListDatas.size() == 0);
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
                if (view.getPaging().getPage() == 1)
                    clearData();
                view.getPaging().setNewParameter(Result);
                finishConnection();
                modelNewOrder = model;
                ListDatas.addAll(modelNewOrder.DataList);
                view.notifyDataSetChanged(ListDatas);
                view.setRefreshPullEnable(true);
            }

            @Override
            public void OnNoResult() {
                finishConnection();
                if (view.getPaging().getPage() == 1)
                    clearData();
                view.getPaging().setHasNext(false);
                view.setRefreshPullEnable(true);
                view.removeRetry();
            }

            @Override
            public void OnNoNextPage() {
                view.getPaging().setHasNext(false);
            }

            @Override
            public void OnError() {
                finishConnection();
                if (ListDatas.size() == 0) {
                    view.addRetry();
                } else {
                    NetworkErrorHelper.showSnackbar((Activity) context);
                }
                view.setRefreshPullEnable(true);
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
