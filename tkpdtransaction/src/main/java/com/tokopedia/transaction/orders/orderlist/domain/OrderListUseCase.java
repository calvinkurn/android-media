package com.tokopedia.transaction.orders.orderlist.domain;

import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.transaction.orders.orderlist.data.OrderCategory;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import rx.Observable;

public class OrderListUseCase extends UseCase<Data> {
    public static final String ORDER_CATEGORY = "orderCategory";
    public static final String PAGE = "Page";
    public static final String PER_PAGE = "PerPage";
    public static final String PAGE_NUM = "page_num";
    protected OrderListRepository orderListRepository;

    public OrderListUseCase(OrderListRepository orderRepository){
        super();
        orderListRepository = orderRepository;
    }

    @Override
    public Observable<Data> createObservable(RequestParams requestParams) {
        return orderListRepository.getOrderList(requestParams) ;
    }

    public RequestParams getUserAttrParam(OrderCategory orderCategory, RequestParams requestParams){
        RequestParams params = RequestParams.create();
        params.putObject(ORDER_CATEGORY, orderCategory);
        int page = requestParams.getInt(PAGE_NUM,1);
        params.putInt(PAGE,page);
        params.putInt(PER_PAGE, 10);
        return params;
    }
}