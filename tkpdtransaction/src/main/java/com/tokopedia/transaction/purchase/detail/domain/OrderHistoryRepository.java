package com.tokopedia.transaction.purchase.detail.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
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

    public OrderHistoryRepository(OrderDetailService service) {
        this.service = service;
    }

    @Override
    public Observable<OrderHistoryData> requestOrderHistoryData(TKPDMapParam<String, Object> params) {
        return service.getApi().getOrderHistory(params).map(new Func1<Response<String>, OrderHistoryData>() {
            @Override
            public OrderHistoryData call(Response<String> stringResponse) {
                return getOrderHistoryData(
                        new Gson().fromJson(stringResponse.body(),OrderHistoryResponse.class));
            }
        });
    }

    private OrderHistoryData getOrderHistoryData(OrderHistoryResponse response) {
        OrderHistoryData viewData = new OrderHistoryData();
        com.tokopedia.transaction.purchase.detail.model.history.response
                .Data historyData = response.getData();
        viewData.setStepperMode(historyData.getOrderStatusCode());
        viewData.setStepperStatusTitle(historyData.getHistoryTitle());
        if(response.getData().getHistoryImg() != null) {
            viewData.setHistoryImage(historyData.getHistoryImg());
        } else viewData.setHistoryImage("");
        List<OrderHistoryListData> historyListData = new ArrayList<>();
        List<History> orderHistories = historyData.getHistories();
        for(int i = 0; i < orderHistories.size(); i++) {
            OrderHistoryListData listData = new OrderHistoryListData();
            listData.setOrderHistoryDate(orderHistories.get(i).getDate());
            listData.setActionBy(orderHistories.get(i).getActionBy());
            listData.setOrderHistoryTitle(orderHistories.get(i).getStatus());
            listData.setColor(orderHistories.get(i).getOrderStatusColor());
            listData.setOrderHistoryTime(orderHistories.get(i).getHour());
            listData.setOrderHistoryComment(orderHistories.get(i).getComment());
            historyListData.add(listData);
        }
        viewData.setOrderListData(historyListData);
        return viewData;
    }
}
