package com.tokopedia.transaction.purchase.detail.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.exception.ResponseRuntimeException;
import com.tokopedia.transaction.purchase.detail.domain.mapper.OrderDetailMapper;
import com.tokopedia.transaction.purchase.detail.model.detail.response.OrderDetailResponse;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 11/9/17. Tokopedia
 */

public class OrderDetailRepository implements IOrderDetailRepository {

    private OrderDetailMapper mapper;

    private OrderDetailService service;

    private ReplacementActService replacementService;

    private TXOrderActService orderActService;

    public OrderDetailRepository(OrderDetailMapper mapper,
                                 OrderDetailService service,
                                 ReplacementActService replacementService,
                                 TXOrderActService orderActService) {
        this.mapper = mapper;
        this.service = service;
        this.replacementService = replacementService;
        this.orderActService = orderActService;
    }

    @Override
    public Observable<OrderDetailData> requestOrderDetailData(TKPDMapParam<String, Object> params) {
        return service.getApi().getOrderDetail(params)
                .map(new Func1<Response<TkpdResponse>, OrderDetailData>() {
                    @Override
                    public OrderDetailData call(Response<TkpdResponse> stringResponse) {
                        validateData(stringResponse.body());
                        OrderDetailResponse response = new Gson().fromJson(stringResponse.body().getStringData(),
                                OrderDetailResponse.class);
                        return mapper.generateOrderDetailModel(response);
                    }
                });
    }

    @Override
    public Observable<String> requestCancelOrder(TKPDMapParam<String, String> params) {
        return orderActService.getApi().requestCancelOrder(params).map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> tkpdResponseResponse) {
                return mapper.getSuccessCancelOrder(tkpdResponseResponse);
            }
        });
    }

    @Override
    public Observable<String> cancelReplacement(TKPDMapParam<String, Object> params) {
        return replacementService.getApi().cancelReplacement(params)
                .map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> tkpdResponseResponse) {
                return mapper.getCancelReplacement(tkpdResponseResponse);
            }
        });
    }

    @Override
    public Observable<String> confirmFinishDeliver(TKPDMapParam<String, String> params) {
        return orderActService.getApi().deliveryFinishOrder(params).map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> response) {
                return mapper.getConfirmDeliverMessage(response);
            }
        });
    }

    @Override
    public Observable<String> confirmDelivery(TKPDMapParam<String, String> params) {
        return orderActService.getApi().deliveryConfirm(params)
                .map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> response) {
                return mapper.getConfirmDeliverMessage(response);
            }
        });
    }

    private void validateData(TkpdResponse response) {
        if (response == null)
            throw new ResponseRuntimeException("Terjadi Kesalahan");
        else if(response.isError()) {
            throw new ResponseRuntimeException(response.getErrorMessageJoined());
        }
    }

}
