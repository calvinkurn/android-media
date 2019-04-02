package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.transaction.orders.orderlist.data.ConditionalInfo;
import com.tokopedia.transaction.orders.orderlist.data.PaymentData;

import java.util.List;

/**
 * Created by baghira on 10/05/18.
 */

public class OrderDetails {
    @SerializedName("status")
    @Expose
    private Status status;
    @SerializedName("conditionalInfo")
    @Expose
    private ConditionalInfo conditionalInfo;
    @SerializedName("title")
    @Expose
    private List<Title> title;
    @SerializedName("invoice")
    @Expose
    private Invoice invoice;
    @SerializedName("orderToken")
    @Expose
    private OrderToken orderToken;
    @SerializedName("detail")
    @Expose
    private List<Detail> detail;
    @SerializedName("additionalInfo")
    @Expose
    private List<AdditionalInfo> additionalInfo;
    @SerializedName("pricing")
    @Expose
    private List<Pricing> pricing;

    @SerializedName("paymentMethod")
    @Expose
    private PaymentMethod paymentMethod;

    @SerializedName("payMethod")
    @Expose
    private List<PayMethod> payMethods;

    @SerializedName("paymentData")
    @Expose
    private PaymentData paymentData;
    @SerializedName("contactUs")
    @Expose
    private ContactUs contactUs;
    @SerializedName("actionButtons")
    @Expose
    private List<ActionButton> actionButtons;
    @SerializedName("items")
    @Expose
    private List<Items> items;

    @SerializedName("driverDetails")
    @Expose
    private DriverDetails driverDetails;

    @SerializedName("dropShipper")
    @Expose
    private DropShipper dropShipper;

    @SerializedName("shopDetails")
    @Expose
    private ShopInfo shopInfo;



    public OrderDetails(Status status, ConditionalInfo conditionalInfo, List<Title> title, Invoice invoice, OrderToken orderToken, List<Detail> detail, List<AdditionalInfo> additionalInfo, List<Pricing> pricing, PaymentMethod paymentMethod, List<PayMethod> payMethods, PaymentData paymentData, ContactUs contactUs, List<ActionButton> actionButtons, List<Items> items, DriverDetails driverDetails, DropShipper dropShipper, ShopInfo shopInfo) {
        this.status = status;
        this.conditionalInfo = conditionalInfo;
        this.title = title;
        this.invoice = invoice;
        this.orderToken = orderToken;
        this.detail = detail;
        this.additionalInfo = additionalInfo;
        this.pricing = pricing;
        this.paymentMethod = paymentMethod;
        this.payMethods = payMethods;
        this.paymentData = paymentData;
        this.contactUs = contactUs;
        this.actionButtons = actionButtons;
        this.items = items;
        this.driverDetails = driverDetails;
        this.dropShipper = dropShipper;
        this.shopInfo = shopInfo;
    }

    public Status status() {
        return status;
    }

    public ConditionalInfo conditionalInfo() {
        return conditionalInfo;
    }

    public List<Title> title() {
        return title;
    }

    public Invoice invoice() {
        return invoice;
    }

    public OrderToken orderToken() {
        return orderToken;
    }

    public List<Detail> detail() {
        return detail;
    }

    public List<AdditionalInfo> additionalInfo() {
        return additionalInfo;
    }

    public List<Pricing> pricing() {
        return pricing;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public List<PayMethod> getPayMethods() {
        return payMethods;
    }

    public PaymentData paymentData() {
        return paymentData;
    }

    public ContactUs contactUs() {
        return contactUs;
    }

    public List<ActionButton> actionButtons() {
        return actionButtons;
    }

    public List<Items> getItems() {
        return items;
    }

    public DriverDetails getDriverDetails() {
        return driverDetails;
    }

    public DropShipper getDropShipper() {
        return dropShipper;
    }

    public ShopInfo getShopInfo() {
        return shopInfo;
    }

    @Override
    public String toString() {
        return "[OrderDetails:{"
                + "status="+status +","
                + "conditionalInfo="+conditionalInfo +","
                + "title="+title +","
                + "invoice="+invoice +","
                + "orderToken="+orderToken +","
                + "detail="+detail +","
                + "additionalInfo="+additionalInfo +","
                + "pricing="+pricing +","
                + "paymentMethod="+paymentMethod +","
                + "paymethods="+payMethods +","
                + "paymentData="+paymentData +","
                + "contactUs="+contactUs +","
                + "actionButtons="+actionButtons + ","
                + "items="+items + ","
                + "driverDetails="+driverDetails +","
                + "dropShipper="+dropShipper + ","
                + "shopInfo="+shopInfo
                + "}]";
    }
}
