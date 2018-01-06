package com.tokopedia.transaction.purchase.detail.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.shop.MyShopOrderService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.network.MyShopOrderActService;
import com.tokopedia.transaction.purchase.detail.domain.mapper.OrderDetailMapper;
import com.tokopedia.transaction.purchase.detail.model.detail.response.courierlist.CourierResponse;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ListCourierViewModel;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by kris on 1/4/18. Tokopedia
 */

public class OrderCourierRepository implements IOrderCourierRepository{

    private OrderDetailMapper mapper;

    private MyShopOrderService service;

    public OrderCourierRepository(OrderDetailMapper mapper, MyShopOrderService shopService) {
        this.mapper = mapper;
        this.service = shopService;
    }

    @Override
    public Observable<ListCourierViewModel> onOrderCourierRepository(
            TKPDMapParam<String, String> params
    ) {
        return service.getApi().getEditShippingForm(params).map(new Func1<Response<TkpdResponse>, ListCourierViewModel>() {
            @Override
            public ListCourierViewModel call(Response<TkpdResponse> tkpdResponseResponse) {
                return mapper.getCourierServiceModel(
                        new Gson().fromJson(tkpdResponseResponse.body().getStringData(),
                                CourierResponse.class)
                );
            }
        });
    }
}
