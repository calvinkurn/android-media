package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
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
    public OrderListPresenterImpl(GraphqlUseCase getOrderListUseCase) {
        this.getOrderListUseCase = getOrderListUseCase;

    }

    @Override
    public void getAllOrderData(Context context, String orderCategory, final int typeRequest, int page) {
        getView().showProcessGetData(orderCategory);
        Map<String, Object> variables = new HashMap<>();
        variables.put("orderCategory", orderCategory);
        variables.put("Page", page);
        variables.put("PerPage", 10);

        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(getView().getAppContext().getResources(),
                R.raw.orderlist), Data.class, variables);


        getOrderListUseCase.setRequest(graphqlRequest);

        getOrderListUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper(e.toString());
                getView().removeProgressBarView();
                getView().unregisterScrollListener();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                } else if (e instanceof SocketTimeoutException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                getView().removeProgressBarView();
                if (response != null) {
                    Data data = response.getData(Data.class);
                    if (!data.orders().isEmpty()) {
                        getView().renderDataList(data.orders());
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
