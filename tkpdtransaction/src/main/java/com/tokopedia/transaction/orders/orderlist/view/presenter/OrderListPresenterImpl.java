package com.tokopedia.transaction.orders.orderlist.view.presenter;

import android.content.Context;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListUseCase;
import com.tokopedia.usecase.RequestParams;

import javax.inject.Inject;

import rx.Subscriber;

public class OrderListPresenterImpl extends BaseDaggerPresenter<OrderListContract.View> implements OrderListContract.Presenter {
    private OrderListUseCase getOrderListUseCase;

    @Inject
    public OrderListPresenterImpl(OrderListUseCase getOrderListUseCase) {
        this.getOrderListUseCase = getOrderListUseCase;

    }

    @Override
    public void getAllOrderData(Context context, OrderCategory orderCategory, int typeRequest, int page) {
        getView().showProcessGetData(orderCategory, typeRequest);
        RequestParams params = RequestParams.create();
        params.putInt(OrderListUseCase.PAGE_NUM, page);
        getOrderListUseCase.execute(getOrderListUseCase.getUserAttrParam(orderCategory, params), new Subscriber<Data>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                Log.e("sandeep", "error is :"+e);
                getView().removeProgressBarView();
                getView().unregisterScrollListener();
            }

            @Override
            public void onNext(Data s) {
                getView().removeProgressBarView();
                if (s != null && !s.orders().isEmpty()) {
                    getView().renderDataList(s.orders());
                } else if(s.orders().isEmpty()){
                    getView().unregisterScrollListener();
                }
            }
        });
    }

}
