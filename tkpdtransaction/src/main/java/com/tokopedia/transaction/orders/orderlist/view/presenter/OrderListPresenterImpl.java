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

    private static final String SEARCH = "Search";
    private static final String START_DATE = "StartDate";
    private static final String END_DATE = "EndDate";
    private static final String SORT = "Sort";
    private static final String ORDER_STATUS = "OrderStatus";
    private static final String ORDER_ID = "orderId";
    private static final int PER_PAGE_COUNT = 10;
    GraphqlUseCase getOrderListUseCase;

    @Inject
    public OrderListPresenterImpl() {

    }

    @Override
    public void getAllOrderData(Context context, String orderCategory, final int typeRequest, int page, int orderId) {
        if (getView() == null || getView().getAppContext() == null)
            return;
        getView().showProcessGetData();
        GraphqlRequest graphqlRequest;
        Map<String, Object> variables = new HashMap<>();

        if (orderCategory.equalsIgnoreCase("MarketPlace")) {
            variables.put(OrderCategory.KEY_LABEL, orderCategory);
            variables.put(OrderCategory.PAGE, page);
            variables.put(OrderCategory.PER_PAGE, PER_PAGE_COUNT);
            variables.put(SEARCH, getView().getSearchedString());
            variables.put(START_DATE, getView().getStartDate());
            variables.put(END_DATE, getView().getEndDate());
            variables.put(SORT, "");
            variables.put(ORDER_STATUS, Integer.parseInt(getView().getSelectedFilter()));
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderlist_marketplace), Data.class, variables);
        } else {
            variables.put(OrderCategory.KEY_LABEL, orderCategory);
            variables.put(OrderCategory.PAGE, page);
            variables.put(OrderCategory.PER_PAGE, PER_PAGE_COUNT);
            variables.put(ORDER_ID, orderId);
            graphqlRequest = new
                    GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                    R.raw.orderlist), Data.class, variables);
        }
        getOrderListUseCase = new GraphqlUseCase();
        getOrderListUseCase.clearRequest();
        getOrderListUseCase.addRequest(graphqlRequest);

        getOrderListUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (getView() != null && getView().getAppContext()!=null) {
                    CommonUtils.dumper("error =" + e.toString());
                    getView().removeProgressBarView();
                    getView().unregisterScrollListener();
                    getView().showErrorNetwork(
                            ErrorHandler.getErrorMessage(getView().getAppContext(), e));
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                if (getView() == null || getView().getAppContext() == null)
                    return;
                getView().removeProgressBarView();
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
        if (isViewAttached()) {
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
    }

    @Override
    public void detachView() {
        getOrderListUseCase.unsubscribe();
        super.detachView();
    }
}