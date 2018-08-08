package com.tokopedia.transaction.purchase.detail.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.domain.mapper.OrderDetailMapper;
import com.tokopedia.transaction.purchase.detail.model.history.response.History;
import com.tokopedia.transaction.purchase.detail.model.history.response.OrderHistoryResponse;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryListData;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 11/17/17. Tokopedia
 */

public class OrderHistoryRepository implements IOrderHistoryRepository{

    private OrderDetailService service;
    private OrderDetailMapper mapper;

    public OrderHistoryRepository(OrderDetailService service, OrderDetailMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @Override
    public Observable<OrderHistoryData> requestOrderHistoryData(TKPDMapParam<String, Object> params) {
        return service.getApi().getOrderHistory(params).map(new Func1<Response<String>, OrderHistoryData>() {
            @Override
            public OrderHistoryData call(Response<String> stringResponse) {
                return mapper.getOrderHistoryData(
                        new Gson().fromJson(stringResponse.body(),OrderHistoryResponse.class));
            }
        });
    }
}
