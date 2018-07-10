package com.tokopedia.transaction.orders.orderdetails.domain;

import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

public class OrderDetailsUseCase extends UseCase<DetailsData> {
    public static final String ORDER_CATEGORY = "orderCategory";
    public static final String ORDER_ID = "orderId";
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

    public RequestParams getUserAttrParam(String orderCategory, String orderId){
        RequestParams params = RequestParams.create();
        params.putObject(ORDER_CATEGORY, orderCategory);
        params.putString(ORDER_ID, orderId);
        return params;
    }
}