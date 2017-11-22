package com.tokopedia.transaction.purchase.detail.domain;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderActService;
import com.tokopedia.core.network.apiservices.transaction.TXOrderService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.exception.ResponseRuntimeException;
import com.tokopedia.transaction.purchase.detail.model.detail.response.Buttons;
import com.tokopedia.transaction.purchase.detail.model.detail.response.Data;
import com.tokopedia.transaction.purchase.detail.model.detail.response.OrderDetailResponse;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ButtonData;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailItemData;
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
 * Created by kris on 11/9/17. Tokopedia
 */

public class OrderDetailRepository implements IOrderDetailRepository {

    private OrderDetailService service;

    private TXOrderService orderService;

    private TXOrderActService orderActService;

    public OrderDetailRepository(OrderDetailService service,
                                 TXOrderService orderService,
                                 TXOrderActService orderActService) {
        this.service = service;
        this.orderService = orderService;
        this.orderActService = orderActService;
    }

    @Override
    public Observable<OrderDetailData> requestOrderDetailData(TKPDMapParam<String, Object> params) {
        return service.getApi().getOrderDetail(params).map(new Func1<Response<String>, OrderDetailData>() {
            @Override
            public OrderDetailData call(Response<String> stringResponse) {
                return generateOrderDetailModel(
                        new Gson().fromJson(stringResponse.body(), OrderDetailResponse.class));
            }
        });
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

    @Override
    public Observable<String> confirmFinishDeliver(TKPDMapParam<String, String> params) {
        return orderActService.getApi().deliveryFinishOrder(params).map(new Func1<Response<TkpdResponse>, String>() {
            @Override
            public String call(Response<TkpdResponse> response) {
                return getConfirmDeliverMessage(response);
            }
        });
    }


    private OrderDetailData generateOrderDetailModel(OrderDetailResponse response) {
        validateData(response);
        OrderDetailData viewData = new OrderDetailData();
        Data responseData = response.getData();
        viewData.setOrderId(String.valueOf(responseData.getOrderId()));
        viewData.setOrderStatus(responseData.getStatus().getDetail());
        viewData.setResoId(String.valueOf(responseData.getResoId()));
        viewData.setOrderImage(responseData.getStatus().getImage());

        viewData.setBuyerName(responseData.getDetail().getReceiver().getName());
        viewData.setPurchaseDate(responseData.getDetail().getPaymentVerifiedDate());
        if(responseData.getDetail().getDeadline() != null) {
            viewData.setResponseTimeLimit(responseData.getDetail().getDeadline().getText());
        }
        if(responseData.getDetail().getShop() !=null) {
            viewData.setShopId(String.valueOf(responseData.getDetail().getShop().getId()));
            viewData.setShopName(responseData.getDetail().getShop().getName());

        }
        viewData.setPartialOrderStatus(
                getPartialOrderStatus(responseData.getDetail().getPartialOrder())
        );
        if(responseData.getDetail().getPreorder() == null
                || responseData.getDetail().getPreorder().getIsPreorder() == 0) {
            viewData.setPreorder(false);
        } else {
            viewData.setPreorder(true);
            viewData.setPreorderPeriod(String.valueOf(
                    responseData.getDetail().getPreorder().getProcessTime())
            );
        }
        if(responseData.getDetail().getDropShipper() != null) {
            viewData.setDropshipperName(responseData.getDetail().getDropShipper().getName());
            viewData.setDropshipperPhone(responseData.getDetail().getDropShipper().getPhone());
        }

        viewData.setShippingAddress(
                responseData.getDetail().getReceiver().getName() + "\n"
                + responseData.getDetail().getReceiver().getPhone() + "\n"
                + responseData.getDetail().getReceiver().getStreet() + "\n"
                + responseData.getDetail().getReceiver().getDistrict() + "\n"
                + responseData.getDetail().getReceiver().getCity() + " "
                + responseData.getDetail().getReceiver().getProvince() + " "
                + responseData.getDetail().getReceiver().getPostal() + " "
        );

        viewData.setInvoiceNumber(responseData.getInvoice());
        viewData.setInvoiceUrl(responseData.getInvoiceUrl());
        viewData.setCourierName(responseData.getDetail().getShipment().getName() + " "
                + responseData.getDetail().getShipment().getProductName());

        viewData.setTotalItemQuantity(String.valueOf(responseData.getSummary().getTotalItem()));
        viewData.setAdditionalFee(responseData.getSummary().getAdditionalPrice());
        viewData.setDeliveryPrice(responseData.getSummary().getShippingPrice());
        viewData.setInsurancePrice(responseData.getSummary().getInsurancePrice());
        viewData.setProductPrice(responseData.getSummary().getItemsPrice());
        viewData.setTotalPayment(responseData.getSummary().getTotalPrice());

        List<OrderDetailItemData> productList = new ArrayList<>();
        for (int i = 0; i < responseData.getProducts().size(); i++) {
            OrderDetailItemData product = new OrderDetailItemData();
            product.setProductId(String.valueOf(responseData.getProducts().get(i).getId()));
            product.setItemName(responseData.getProducts().get(i).getName());
            product.setDescription(responseData.getProducts().get(i).getNote());
            product.setItemQuantity(String.valueOf(responseData.getProducts().get(i).getQuantity()));
            product.setPrice(responseData.getProducts().get(i).getPrice());
            product.setImageUrl(responseData.getProducts().get(i).getThumbnail());
            productList.add(product);
        }
        viewData.setItemList(productList);

        ButtonData buttonData = new ButtonData();
        Buttons buttons = responseData.getButtons();
        buttonData.setAcceptOrderVisibility(buttons.getAcceptOrder());
        buttonData.setAskBuyerVisibility(buttons.getAskBuyer());
        buttonData.setAskSellerVisibility(buttons.getAskSeller());
        buttonData.setCancelPeluangVisibility(buttons.getCancelPeluang());
        buttonData.setChangeAwbVisibility(buttons.getChangeAwb());
        buttonData.setChangeCourier(buttons.getChangeCourier());
        buttonData.setComplaintVisibility(buttons.getComplaint());
        buttonData.setViewComplaint(buttons.getViewComplaint());
        buttonData.setConfirmShippingVisibility(buttons.getConfirmShipping());
        buttonData.setFinishOrderVisibility(buttons.getFinishOrder());
        buttonData.setRejectOrderVisibility(buttons.getRejectOrder());
        buttonData.setRequestCancelVisibility(buttons.getRequestCancel());
        buttonData.setOrderDetailVisibility(buttons.getOrderDetail());
        buttonData.setReceiveConfirmationVisibility(buttons.getReceiveConfirmation());
        buttonData.setTrackVisibility(buttons.getTrack());
        buttonData.setRequestPickupVisibility(buttons.getRequestPickup());
        viewData.setButtonData(buttonData);

        return viewData;
    }

    private void validateData(OrderDetailResponse response) {
        if(response.getData() == null) {
            throw new ResponseRuntimeException("Terjadi Kesalahan");
        } //TODO add another
    }

    private OrderHistoryData getOrderHistoryData(OrderHistoryResponse response) {
        OrderHistoryData viewData = new OrderHistoryData();
        com.tokopedia.transaction.purchase.detail.model.history.response
                .Data historyData = response.getData();
        viewData.setStepperMode(historyData.getOrderStatusCode());
        viewData.setStepperStatusTitle(historyData.getOrderStatus());
        List<OrderHistoryListData> historyListData = new ArrayList<>();
        List<History> orderHistories = historyData.getHistories();
        for(int i = 0; i < orderHistories.size(); i++) {
            OrderHistoryListData listData = new OrderHistoryListData();
            listData.setOrderHistoryDate(orderHistories.get(i).getDate());
            listData.setActionBy(orderHistories.get(i).getActionBy());
            listData.setOrderHistoryTitle(orderHistories.get(i).getStatus());
            listData.setColor(orderHistories.get(i).getOrderStatusColor());
            listData.setOrderHistoryTime(orderHistories.get(i).getHour());
            historyListData.add(listData);
        }
        viewData.setOrderListData(historyListData);
        return viewData;
    }

    private String getConfirmDeliverMessage(Response<TkpdResponse> response) {
        return response.body().getStatusMessageJoined();
    }

    private String getPartialOrderStatus(int partialOrder) {
        if(partialOrder == 1) return "Ya";
        else return "Tidak";
    }

}
