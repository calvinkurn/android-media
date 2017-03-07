package com.tokopedia.seller.opportunity.viewmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.util.MethodChecker;

import java.util.List;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityItemViewModel {

    private int orderJOBStatus;
    private int orderIsPickup;
    private int orderShippingRetry;
    private OrderCustomerViewModel orderCustomer;
    private Object orderPayment;
    private OrderDetailViewModel orderDetail;
    private String orderAutoResi;
    private OrderDeadlineViewModel orderDeadline;
    private int orderAutoAwb;
    private OrderShopViewModel orderShop;
    private List<OrderProductViewModel> orderProducts = null;
    private OrderShipmentViewModel orderShipment;
    private Object orderLast;
    private Object orderHistory;
    private Object orderJOBDetail;
    private OrderDestinationViewModel orderDestination;

    //TODO : Delete later
    private String productName;
    private String productPrice;
    private String productImage;
    private String deadline;

    public OpportunityItemViewModel(String productName) {
        this.productName = productName;
        this.productPrice = "Rp 10.000.000";
        this.deadline = "24 jam lagi";
        this.productImage = "https://pbs.twimg.com/media/C51fHIHVAAQH2WZ.jpg";
    }

    public String getProductName() {
        return MethodChecker.fromHtml(productName).toString();
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    //End of TODO
}
