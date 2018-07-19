package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderlist.data.Data;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.HashMap;
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
        getView().showProcessGetData(orderCategory);
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderCategory", orderCategory);
        variables.put("Page", page);
        variables.put("PerPage", 10);
        variables.put("orderId", orderId);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.orderlist), Data.class, variables);
        getOrderListUseCase = new GraphqlUseCase();
        getOrderListUseCase.clearRequest();
        getOrderListUseCase.setRequest(graphqlRequest);

        getOrderListUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper("error ="+e.toString());
                getView().removeProgressBarView();
                getView().unregisterScrollListener();
                getView().showErrorNetwork(
                        ErrorHandler.getErrorMessage(getView().getAppContext(), e));
            }

            @Override
            public void onNext(GraphqlResponse response) {
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
}
