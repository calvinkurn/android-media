package com.tokopedia.transaction.purchase.detail.domain.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.payment.utils.ErrorNetMessage;
import com.tokopedia.transaction.exception.ResponseRuntimeException;
import com.tokopedia.transaction.purchase.detail.model.detail.response.Buttons;
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

/**
 * Created by kris on 11/23/17. Tokopedia
 */

public class OrderDetailMapper {

    public OrderDetailData generateOrderDetailModel(OrderDetailResponse responseData) {
        OrderDetailData viewData = new OrderDetailData();
        viewData.setOrderId(String.valueOf(responseData.getOrderId()));
        viewData.setOrderCode(String.valueOf(responseData.getOrderStatus()));
        viewData.setOrderStatus(responseData.getStatus().getDetail());
        viewData.setResoId(String.valueOf(responseData.getResoId()));
        viewData.setOrderImage(responseData.getStatus().getImage());

        viewData.setBuyerName(responseData.getDetail().getReceiver().getName());
        if (responseData.getDetail().getPaymentVerifiedDate() != null) {
            viewData.setPurchaseDate(responseData.getDetail().getPaymentVerifiedDate());
        } else {
            viewData.setPurchaseDate(responseData.getDetail().getCheckoutDate());
        }
        if(responseData.getDetail().getDeadline() != null) {
            viewData.setResponseTimeLimit(responseData.getDetail().getDeadline().getText());
            viewData.setDeadlineColorString(responseData.getDetail().getDeadline().getColor());
        }
        if(responseData.getDetail().getShop() != null) {
            viewData.setShopId(String.valueOf(responseData.getDetail().getShop().getId()));
            viewData.setShopName(responseData.getDetail().getShop().getName());
            viewData.setShopLogo(responseData.getDetail().getShop().getLogo());
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
                    responseData.getDetail().getPreorder().getProcessTime()));
            viewData.setPreorderPeriodText(responseData.getDetail().getPreorder().getProcessTimeText());
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

        if (responseData.getDetail().getShipment().getInfo() != null &&
                responseData.getDetail().getShipment().getInfo().getDriver() != null) {
            viewData.setDriverName(
                    responseData.getDetail().getShipment().getInfo().getDriver().getName()
            );
            viewData.setDriverImage(
                    responseData.getDetail().getShipment().getInfo().getDriver().getPhoto()
            );
            viewData.setDriverPhone(
                    responseData.getDetail().getShipment().getInfo().getDriver().getPhone()
            );
            viewData.setDriverVehicle(
                    responseData.getDetail().getShipment().getInfo().getDriver().getLicenseNumber()
            );
        }

        if (responseData.getDetail().getShipment().getInfo() != null &&
                responseData.getDetail().getShipment().getInfo().getPickupInfo() != null) {
            viewData.setPickupPinCode(responseData.getDetail().getShipment().getInfo()
                    .getPickupInfo().getPickupCode());
        }

        return viewData;
    }

    public OrderHistoryData getOrderHistoryData(OrderHistoryResponse response) {
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

    private String getPartialOrderStatus(int partialOrder) {
        if(partialOrder == 1) return "Ya";
        else return "Tidak";
    }

    public String getConfirmDeliverMessage(Response<TkpdResponse> response) {
        handleResponseError(response.body());
        return response.body().getStatusMessageJoined();
    }

    public String getCancelReplacement(Response<TkpdResponse> response) {
        handleResponseError(response.body());
        return response.body().getStatusMessageJoined();
    }

    public String getSuccessCancelOrder(Response<TkpdResponse> response) {
        handleResponseError(response.body());
        return response.body().getStatusMessageJoined();
    }

    private void handleResponseError(TkpdResponse response) {
        if(response == null)
            throw new ResponseRuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
        else if(response.isError())
            throw new ResponseRuntimeException(response.getErrorMessageJoined());
    }

}
