package com.tokopedia.transaction.orders.orderlist.view.presenter;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.utils.GraphqlHelper;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.orders.orderlist.data.TabData;

import rx.Subscriber;

public class OrderListInitPresenterImpl implements OrderListInitContract.Presenter {
    OrderListInitContract.View view;
    GraphqlUseCase initUseCase;
    public OrderListInitPresenterImpl(OrderListInitContract.View view, GraphqlUseCase initUseCase) {
        this.view = view;
        this.initUseCase = initUseCase;
    }


    @Override
    public void getInitData() {
        GraphqlRequest graphqlRequest = new
                GraphqlRequest(GraphqlHelper.loadRawString(view.getAppContext().getResources(),
                R.raw.initorderlist), TabData.class, false);
        initUseCase.addRequest(graphqlRequest);

        initUseCase.execute(new Subscriber<GraphqlResponse>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                CommonUtils.dumper(e.toString());
                view.removeProgressBarView();
                view.showErrorNetwork(
                        ErrorHandler.getErrorMessage(view.getAppContext(), e));
            }

            @Override
            public void onNext(GraphqlResponse response) {
                view.removeProgressBarView();
                if (response != null) {
                    TabData data = response.getData(TabData.class);
                    if (!data.getOrderLabelList().isEmpty()) {
                        view.renderTabs(data.getOrderLabelList());
                    }
                }
            }
        });
    }

    @Override
    public void destroyView() {
        initUseCase.unsubscribe();
    }
}
