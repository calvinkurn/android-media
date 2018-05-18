package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListUseCase;
import com.tokopedia.usecase.RequestParams;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.inject.Inject;

import rx.Subscriber;

public class OrderListPresenterImpl extends BaseDaggerPresenter<OrderListContract.View> implements OrderListContract.Presenter {
    private OrderListUseCase getOrderListUseCase;

    @Inject
    public OrderListPresenterImpl(OrderListUseCase getOrderListUseCase) {
        this.getOrderListUseCase = getOrderListUseCase;

    }

    @Override
    public void getAllOrderData(Context context, OrderCategory orderCategory, final int typeRequest, int page) {
        getView().showProcessGetData(orderCategory);
        RequestParams params = RequestParams.create();
        params.putInt(OrderListUseCase.PAGE_NUM, page);
        getOrderListUseCase.execute(getOrderListUseCase.getUserAttrParam(orderCategory, params), new Subscriber<Data>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("sandeep", "error is :" + e);
                getView().removeProgressBarView();
                getView().unregisterScrollListener();
                if (e instanceof UnknownHostException || e instanceof ConnectException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_NO_CONNECTION_FULL);
                } else if (e instanceof SocketTimeoutException) {
                    getView().showErrorNetwork(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                }
            }

            @Override
            public void onNext(Data data) {
                getView().removeProgressBarView();
                if (data != null) {
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
