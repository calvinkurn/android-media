package com.tokopedia.transaction.purchase.detail.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.domain.mapper.OrderDetailMapper;
import com.tokopedia.transaction.purchase.detail.model.detail.response.OrderDetailResponse;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.history.response.OrderHistoryResponse;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 11/9/17. Tokopedia
 */

public class OrderDetailRepository implements IOrderDetailRepository {

    private OrderDetailMapper mapper;

    private OrderDetailService service;

    private TXOrderActService orderActService;

    public OrderDetailRepository(OrderDetailMapper mapper,
                                 OrderDetailService service,
                                 TXOrderActService orderActService) {
        this.mapper = mapper;
        this.service = service;
        this.orderActService = orderActService;
    }

    @Override
    public Observable<OrderDetailData> requestOrderDetailData(TKPDMapParam<String, Object> params) {
        return service.getApi().getOrderDetail(params).map(new Func1<Response<String>, OrderDetailData>() {
            @Override
            public OrderDetailData call(Response<String> stringResponse) {
                return mapper.generateOrderDetailModel(
                        new Gson().fromJson(stringResponse.body(), OrderDetailResponse.class));
            }
        });
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

    @Override
    public Observable<String> confirmFinishDeliver(TKPDMapParam<String, String> params) {
        return orderActService.getApi().deliveryFinishOrder(params).map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> response) {
                return getConfirmDeliverMessage(response);
            }
        });
    }

    private String getConfirmDeliverMessage(Response<TkpdResponse> response) {
        return response.body().getStatusMessageJoined();
    }

}
