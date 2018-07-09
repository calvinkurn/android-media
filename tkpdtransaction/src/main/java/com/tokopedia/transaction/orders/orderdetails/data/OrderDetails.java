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
    @SerializedName("paymentData")
    @Expose
    private PaymentData paymentData;
    @SerializedName("contactUs")
    @Expose
    private ContactUs contactUs;
    @SerializedName("actionButtons")
    @Expose
    private List<ActionButton> actionButtons;

    public OrderDetails(Status status, ConditionalInfo conditionalInfo, List<Title> title, Invoice invoice, OrderToken orderToken, List<Detail> detail, List<AdditionalInfo> additionalInfo, List<Pricing> pricing, PaymentData paymentData, ContactUs contactUs, List<ActionButton> actionButtons) {
        this.status = status;
        this.conditionalInfo = conditionalInfo;
        this.title = title;
        this.invoice = invoice;
        this.orderToken = orderToken;
        this.detail = detail;
        this.additionalInfo = additionalInfo;
        this.pricing = pricing;
        this.paymentData = paymentData;
        this.contactUs = contactUs;
        this.actionButtons = actionButtons;
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

    public PaymentData paymentData() {
        return paymentData;
    }

    public ContactUs contactUs() {
        return contactUs;
    }

    public List<ActionButton> actionButtons() {
        return actionButtons;
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
                + "paymentData="+paymentData +","
                + "contactUs="+contactUs +","
                + "actionButtons="+actionButtons
                + "}]";
    }
}
