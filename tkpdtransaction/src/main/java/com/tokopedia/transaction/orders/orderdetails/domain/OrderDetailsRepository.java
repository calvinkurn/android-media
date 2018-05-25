package com.tokopedia.transaction.orders.orderdetails.domain;

import com.tokopedia.transaction.orders.orderdetails.data.DetailsData;
import com.tokopedia.transaction.orders.orderlist.data.Data;
import com.tokopedia.usecase.RequestParams;

import rx.Observable;

public interface OrderDetailsRepository {

    Observable<DetailsData> getOrderDetails(RequestParams parameters);

}