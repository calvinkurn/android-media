package com.tokopedia.transaction.purchase.detail.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.shop.MyShopOrderService;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.exception.ResponseRuntimeException;
import com.tokopedia.transaction.network.MyShopOrderActService;
import com.tokopedia.transaction.purchase.detail.domain.mapper.OrderDetailMapper;
import com.tokopedia.transaction.purchase.detail.model.detail.response.courierlist.CourierResponse;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;

import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class OrderCourierRepository implements IOrderCourierRepository{

    private OrderDetailMapper mapper;

    private MyShopOrderService service;

    private MyShopOrderActService actionService;

    private OrderDetailService orderDetailService;

    public OrderCourierRepository(OrderDetailMapper mapper,
                                  MyShopOrderService shopService,
                                  MyShopOrderActService actionService,
                                  OrderDetailService orderDetailService) {
        this.mapper = mapper;
        this.service = shopService;
        this.actionService = actionService;
        this.orderDetailService = orderDetailService;
    }

    @Override
    public Observable<ListCourierViewModel> onOrderCourierRepository(
            final String selectedCourierId,
            TKPDMapParam<String, String> params
    ) {
        return service.getApi().getEditShippingForm(params).map(new Func1<Response<TkpdResponse>, ListCourierViewModel>() {
            @Override
            public ListCourierViewModel call(Response<TkpdResponse> tkpdResponseResponse) {
                return mapper.getCourierServiceModel(
                        new Gson().fromJson(tkpdResponseResponse.body().getStringData(),
                                CourierResponse.class),
                        selectedCourierId
                );
            }
        });
    }

    @Override
    public Observable<String> processShipping(TKPDMapParam<String, String> param) {
        return actionService.getApi().proceedShipping(param)
                .map(new Func1<Response<TkpdResponse>, String>() {
                    @Override
                    public String call(Response<TkpdResponse> tkpdResponseResponse) {
                        return displayMessageToUser(tkpdResponseResponse);
                    }
                });
    }

    @Override
    public Observable<String> changeCourier(Map<String, String> param) {
        return orderDetailService.getApi().changeCourier(param)
                .map(new Func1<Response<TkpdResponse>, String>() {
                    @Override
                    public String call(Response<TkpdResponse> stringResponse) {
                        return displayMessageToUser(stringResponse);
                    }
                });
    }

    private String displayMessageToUser(Response<TkpdResponse> tkpdResponseResponse) {
        if (tkpdResponseResponse.isSuccessful() && !tkpdResponseResponse.body().isError())
            return tkpdResponseResponse.body().getStatusMessageJoined();
        else
            throw new ResponseRuntimeException(
                    tkpdResponseResponse.body().getErrorMessageJoined()
            );
    }
}
