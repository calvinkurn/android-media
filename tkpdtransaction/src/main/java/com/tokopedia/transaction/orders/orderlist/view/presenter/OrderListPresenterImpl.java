package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.design.quickfilter.QuickFilterItem;
import com.tokopedia.design.quickfilter.custom.CustomViewRoundedQuickFilterItem;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.data.FilterStatus;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;

public class OrderListPresenterImpl extends BaseDaggerPresenter<OrderListContract.View> implements OrderListContract.Presenter {
    GraphqlUseCase getOrderListUseCase;

    @Inject
    public OrderListPresenterImpl() {

    }

    @Override
    public void getAllOrderData(Context context, String orderCategory, final int typeRequest, int page, int orderId) {
        if (getView().getAppContext() == null)
            return;
        getView().showProcessGetData(orderCategory);
//        getView().showProgressBar();
        GraphqlRequest graphqlRequest;
        Map<String, Object> variables = new HashMap<>();

        if (orderCategory.equalsIgnoreCase("MarketPlace")) {
            variables.put(OrderCategory.KEY_LABEL, orderCategory);
            variables.put("Page", page);
            variables.put("PerPage", 10);
            variables.put("Search", getView().getSearchedString());
            variables.put("StartDate", "");
            variables.put("EndDate", "");
            variables.put("Sort", "");
            variables.put("OrderStatus", Integer.parseInt(getView().getSelectedFilter()));
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderlist_marketplace), Data.class, variables);
        } else {
            variables.put(OrderCategory.KEY_LABEL, orderCategory);
            variables.put("Page", page);
            variables.put("PerPage", 10);
            variables.put("orderId", orderId);
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderlist), Data.class, variables);
        }
        getOrderListUseCase = new GraphqlUseCase();
        getOrderListUseCase.clearRequest();
        getOrderListUseCase.setRequest(graphqlRequest);

        getOrderListUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("error =" + e.toString());
                getView().removeProgressBarView();
//                getView().hideProgressBar();
                getView().unregisterScrollListener();
                getView().showErrorNetwork(
                        ErrorHandler.getErrorMessage(getView().getAppContext(), e));
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (getView() == null || getView().getAppContext() == null)
                    return;
                getView().removeProgressBarView();
//                getView().hideProgressBar();
                if (response != null) {
                    Data data = response.getData(Data.class);
                    if (!data.orders().isEmpty()) {
                        getView().renderDataList(data.orders());
                        getView().setLastOrderId(data.orders().get(0).getOrderId());
                    } else {
                        getView().unregisterScrollListener();
                        getView().renderEmptyList(typeRequest);
                    }
                } else {
                    getView().unregisterScrollListener();
                    getView().renderEmptyList(typeRequest);
                }

            }
        });
    }


    public void buildAndRenderFilterList(List<FilterStatus> filterItems) {
        List<QuickFilterItem> quickFilterItems = new ArrayList<>();
        boolean isAnyItemSelected = false;
        for (FilterStatus entry : filterItems) {
            CustomViewRoundedQuickFilterItem finishFilter = new CustomViewRoundedQuickFilterItem();
            finishFilter.setName(entry.getFilterName());
            finishFilter.setType(entry.getFilterLabel());
            finishFilter.setColorBorder(R.color.tkpd_main_green);
            if (getView().getSelectedFilter().equalsIgnoreCase(entry.getFilterLabel())) {
                isAnyItemSelected = true;
                finishFilter.setSelected(true);
            } else {
                finishFilter.setSelected(false);
            }
            quickFilterItems.add(finishFilter);
        }
        getView().renderOrderStatus(quickFilterItems);
    }

    public void onDestroy() {
        getOrderListUseCase.unsubscribe();
    }
}
