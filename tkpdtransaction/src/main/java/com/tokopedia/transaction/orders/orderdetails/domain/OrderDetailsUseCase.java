package com.tokopedia.transaction.orders.orderdetails.domain;

import android.util.Log;

import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.transaction.orders.orderlist.domain.OrderListRepository;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class OrderDetailsUseCase extends UseCase<DetailsData> {
    public static final String ORDER_CATEGORY = "orderCategory";
    public static final String ORDER_ID = "orderId";
    public static final String ORDER_DETAIL = "detail";
    public static final String ORDER_ACTION = "action";

    protected OrderDetailsRepository orderDetailsRepository;

    @Inject
    public OrderDetailsUseCase(OrderDetailsRepository orderDetailsRepository){
        super();
        this.orderDetailsRepository = orderDetailsRepository;
    }

    @Override
    public Observable<DetailsData> createObservable(RequestParams requestParams) {
        return orderDetailsRepository.getOrderDetails(requestParams) ;
    }

    public RequestParams getUserAttrParam(String orderCategory, String orderId, int orderDetail, int orderAction){
        RequestParams params = RequestParams.create();
        params.putObject(ORDER_CATEGORY, orderCategory);
        params.putString(ORDER_ID, orderId);
        params.putInt(ORDER_DETAIL, orderDetail);
        params.putInt(ORDER_ACTION, orderAction);
        return params;
    }
}