package com.tokopedia.transaction.purchase.detail.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.replacement.ReplacementActService;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.exception.ResponseRuntimeException;
import com.tokopedia.transaction.network.MyShopOrderActService;
import com.tokopedia.transaction.network.ProductChangeService;
import com.tokopedia.transaction.purchase.detail.domain.mapper.OrderDetailMapper;
import com.tokopedia.transaction.purchase.detail.model.detail.response.OrderDetailResponse;
import com.tokopedia.transaction.common.data.order.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.rejectorder.EmptyVarianProductEditable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private MyShopOrderActService shopService;

    private ProductChangeService productActService;

    public OrderDetailRepository(OrderDetailMapper mapper,
                                 OrderDetailService service,
                                 ReplacementActService replacementService,
                                 TXOrderActService orderActService,
                                 MyShopOrderActService shopService,
                                 ProductChangeService productActService) {
        this.mapper = mapper;
        this.service = service;
        this.replacementService = replacementService;
        this.orderActService = orderActService;
        this.shopService = shopService;
        this.productActService = productActService;
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

    @Override
    public Observable<String> processOrder(TKPDMapParam<String, String> param) {
        return shopService.getApi().proceedOrder(param)
                .map(new Func1<Response<TkpdResponse>, String>() {
                    @Override
                    public String call(Response<TkpdResponse> tkpdResponseResponse) {
                        return displayMessageToUser(tkpdResponseResponse);
                    }
                });
    }

    @Override
    public Observable<String> processShipping(TKPDMapParam<String, String> param) {
        return shopService.getApi().proceedShipping(param)
                .map(new Func1<Response<TkpdResponse>, String>() {
                    @Override
                    public String call(Response<TkpdResponse> tkpdResponseResponse) {
                        return displayMessageToUser(tkpdResponseResponse);
                    }
                });
    }

    @Override
    public Observable<String> changeProduct(Map<String, String> productParam) {
        return productActService.getApi().editWeightPrice(productParam)
                .map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> tkpdResponseResponse) {
                return displayMessageToUser(tkpdResponseResponse);
            }
        });
    }

    @Override
    public Observable<String> retryPickup(TKPDMapParam<String, String> param) {
        return shopService.getApi().retryPickUp(param).map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> tkpdResponseResponse) {
                return displayMessageToUser(tkpdResponseResponse);
            }
        });
    }

    @Override
    public Observable<String> changeAwb(TKPDMapParam<String, String> param) {
        return shopService.getApi().editShippingRef(param)
                .map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> tkpdResponseResponse) {
                return displayMessageToUser(tkpdResponseResponse);
            }
        });
    }

    @Override
    public Observable<String> rejectOrderChangeProductVarian(
            List<EmptyVarianProductEditable> emptyVarianProductEditables,
            TKPDMapParam<String, String> productParam,
            final TKPDMapParam<String, String> rejectParam
    ) {
        return rejectOrderMergedEditDescription(
                emptyVarianProductEditables,
                productParam,
                rejectParam
        );
    }

    private Observable<String> rejectOrderMergedEditDescription(
            List<EmptyVarianProductEditable> emptyVarianProductEditables,
            TKPDMapParam<String, String> productParam,
            TKPDMapParam<String, String> rejectParam
    ) {
        List<Observable<String>> cartVarianObservableList = new ArrayList<>();
        for (int i =0; i < emptyVarianProductEditables.size(); i++) {
            TKPDMapParam<String, String> params = new TKPDMapParam<>();
            params.putAll(productParam);
            params.put(SHOP_ID_KEY, emptyVarianProductEditables.get(i).getShopId());
            params.put(PRODUCT_ID_KEY, emptyVarianProductEditables.get(i).getProductId());
            params.put(PRODUCT_DESCRIPTION_KEY, emptyVarianProductEditables.get(i)
                    .getProductDescription());
            cartVarianObservableList.add(productActService.getApi().editDescription(params));
        }
        cartVarianObservableList.add(processOrder(rejectParam));
        return Observable.concat(cartVarianObservableList);
    }

    private String displayMessageToUser(Response<TkpdResponse> tkpdResponseResponse) {
        if (tkpdResponseResponse.isSuccessful() && !tkpdResponseResponse.body().isError())
            return tkpdResponseResponse.body().getStatusMessageJoined();
        else
            throw new ResponseRuntimeException(
                    tkpdResponseResponse.body().getErrorMessageJoined()
            );
    }

    private void validateData(TkpdResponse response) {
        if (response == null)
            throw new ResponseRuntimeException("Terjadi Kesalahan");
        else if (response.isError()) {
            throw new ResponseRuntimeException(response.getErrorMessageJoined());
        }
    }

}
