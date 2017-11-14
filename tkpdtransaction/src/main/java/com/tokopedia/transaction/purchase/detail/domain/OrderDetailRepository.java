package com.tokopedia.transaction.purchase.detail.domain;


import android.view.View;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.transaction.OrderDetailService;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.purchase.detail.model.detail.response.Buttons;
import com.tokopedia.transaction.purchase.detail.model.detail.response.Data;
import com.tokopedia.transaction.purchase.detail.model.detail.response.OrderDetailResponse;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.ButtonData;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailData;
import com.tokopedia.transaction.purchase.detail.model.detail.viewmodel.OrderDetailItemData;
import com.tokopedia.transaction.purchase.detail.model.history.viewmodel.OrderHistoryData;

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

    public OrderDetailRepository(OrderDetailService service) {
        this.service = service;
    }

    @Override
    public Observable<OrderDetailData> requestOrderDetailData(TKPDMapParam<String, Object> params) {
        return service.getApi().getOrderDetail(params).map(new Func1<Response<String>, OrderDetailData>() {
            @Override
            public OrderDetailData call(Response<String> stringResponse) {
                return generateCreditCardModel(
                        new Gson().fromJson(stringResponse.body(), OrderDetailResponse.class));
            }
        });
    }

    @Override
    public Observable<OrderHistoryData> requestOrderHistoryData(TKPDMapParam<String, String> params) {
        return null;
    }

    private OrderDetailData generateCreditCardModel(OrderDetailResponse response) {
        OrderDetailData viewData = new OrderDetailData();
        Data responseData = response.getData();
        viewData.setOrderStatus(responseData.getStatus().getDetail());
        viewData.setOrderImage(responseData.getStatus().getImage());

        viewData.setBuyerName(responseData.getDetail().getReceiver().getName());
        viewData.setPurchaseDate(responseData.getDetail().getPaymentVerifiedDate());
        viewData.setResponseTimeLimit(responseData.getDetail().getDeadline().getText());
        viewData.setPartialOrderStatus(
                getPartialOrderStatus(responseData.getDetail().getPartialOrder())
        );
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
        viewData.setCourierName(responseData.getDetail().getShipment().getName()
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
            product.setItemName(responseData.getProducts().get(i).getName());
            product.setDescription(responseData.getProducts().get(i).getNote());
            product.setItemQuantity(String.valueOf(responseData.getProducts().get(i).getQuantity()));
            product.setPrice(responseData.getProducts().get(i).getPrice());
            productList.add(product);
        }
        viewData.setItemList(productList);

        ButtonData buttonData = new ButtonData();
        Buttons buttons = responseData.getButtons();
        buttonData.setAcceptOrderVisibility(switchVisibilty(buttons.getAcceptOrder()));
        buttonData.setAskBuyerVisibility(switchVisibilty(buttons.getAskBuyer()));
        buttonData.setAskSellerVisibility(switchVisibilty(buttons.getAskSeller()));
        buttonData.setCancelPeluangVisibility(switchVisibilty(buttons.getCancelPeluang()));
        buttonData.setChangeAwbVisibility(switchVisibilty(buttons.getChangeAwb()));
        buttonData.setChangeCourier(switchVisibilty(buttons.getChangeCourier()));
        buttonData.setComplaintVisibility(switchVisibilty(buttons.getComplaint()));
        buttonData.setViewComplaint(switchVisibilty(buttons.getViewComplaint()));
        buttonData.setConfirmShippingVisibility(switchVisibilty(buttons.getConfirmShipping()));
        buttonData.setFinishOrderVisibility(switchVisibilty(buttons.getFinishOrder()));
        buttonData.setRejectOrderVisibility(switchVisibilty(buttons.getRejectOrder()));
        buttonData.setRequestCancelVisibility(switchVisibilty(buttons.getRequestCancel()));
        buttonData.setOrderDetailVisibility(switchVisibilty(buttons.getOrderDetail()));
        buttonData.setReceiveConfirmationVisibility(switchVisibilty(buttons
                .getReceiveConfirmation()));
        buttonData.setTrackVisibility(switchVisibilty(buttons.getTrack()));
        buttonData.setRequestPickupVisibility(switchVisibilty(buttons.getRequestPickup()));
        viewData.setButtonData(buttonData);

        return viewData;
    }

    private String getPartialOrderStatus(int partialOrder) {
        if(partialOrder == 1) return "Ya";
        else return "Tidak";
    }

    private int switchVisibilty(int responseVisibility) {
        if(responseVisibility == 1) return View.VISIBLE;
        else return View.GONE;
    }
}
