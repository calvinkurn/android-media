package com.tokopedia.transaction.orders.orderlist.view.presenter;

import com.tkpd.library.utils.CommonUtils;
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

import rx.Subscriber;

public class OrderListInitPresenterImpl implements OrderListInitContract.Presenter {
    OrderListInitContract.View view;
    GraphqlUseCase initUseCase;
    public OrderListInitPresenterImpl(OrderListInitContract.View view, GraphqlUseCase initUseCase) {
        this.view = view;
        this.initUseCase = initUseCase;
    }


    @Override
    public void getInitData(String orderCategory, int typeRequest, int page) {
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(view.getAppContext().getResources(),
                R.raw.initorderlist), Data.class);


        initUseCase.setRequest(graphqlRequest);

        initUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper(e.toString());
                view.removeProgressBarView();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    view.showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                } else if (e instanceof SocketTimeoutException) {
                    view.showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                }
            }

            @Override
            public void onNext(GraphqlResponse response) {
                view.removeProgressBarView();
                if (response != null) {
                    Data data = response.getData(Data.class);
                    if (!data.orderLabelList().isEmpty()) {
                        view.renderTabs(data.orderLabelList());
                    } else {
                        //view.renderEmptyList(typeRequest);
                    }
                } else {
                   // view.renderEmptyList(typeRequest);
                }

            }
        });
    }
}
