package com.tokopedia.transaction.orders.orderlist.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Order {
    @SerializedName("conditionalInfo")
    @Expose
    private ConditionalInfo conditionalInfo;
    @SerializedName("paymentData")
    @Expose
    private PaymentData paymentData;
    @SerializedName("paymentID")
    @Expose
    private String paymentID;
    @SerializedName("categoryName")
    @Expose
    private String categoryName;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("appLink")
    @Expose
    private String appLink;
    @SerializedName("upstream")
    @Expose
    private String upstream;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("createdAt")
    @Expose
    private String createdAt;
    @SerializedName("status")
    @Expose
    private int status;
    @SerializedName("statusStr")
    @Expose
    private String statusStr;
    @SerializedName("statusColor")
    @Expose
    private String statusColor;
    @SerializedName("invoiceRefNum")
    @Expose
    private String invoiceRefNum;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("metaData")
    @Expose
    private List<MetaData> metaData;
    @SerializedName("dotMenuList")
    @Expose
    private List<DotMenuList> dotMenuList;
    @SerializedName("dotMenu")
    @Expose
    private List<DotMenuList> dotMenu;
    @SerializedName("actionButtons")
    @Expose
    private List<ActionButton> actionButtons;
    @SerializedName("totalInvoices")
    @Expose
    private String totalInvoices;
    @SerializedName("itemCount")
    @Expose
    private String itemCount;
    @SerializedName("items")
    @Expose
    private List<Item> items;
    @SerializedName("beforeOrderId")
    @Expose
    private int orderId;


    public Order(ConditionalInfo conditionalInfo, PaymentData paymentData, String paymentID,
                 String categoryName, String appLink, String upstream, String category, String id,
                 String createdAt, int status, String statusStr, String statusColor, String invoiceRefNum,
                 String title, List<MetaData> metaData, List<DotMenuList> dotMenuList, List<DotMenuList> dotMenu,
                 List<ActionButton> actionButtons, String totalInvoices, String itemCount, List<Item> items, int orderId) {
        this.conditionalInfo = conditionalInfo;
        this.paymentData = paymentData;
        this.paymentID = paymentID;
        this.categoryName = categoryName;
        this.appLink = appLink;
        this.upstream = upstream;
        this.category = category;
        this.id = id;
        this.createdAt = createdAt;
        this.status = status;
        this.statusStr = statusStr;
        this.statusColor = statusColor;
        this.invoiceRefNum = invoiceRefNum;
        this.title = title;
        this.metaData = metaData;
        this.dotMenuList = dotMenuList;
        this.dotMenu = dotMenu;
        this.actionButtons = actionButtons;
        this.totalInvoices = totalInvoices;
        this.itemCount = itemCount;
        this.items = items;
        this.orderId= orderId;
    }

    public ConditionalInfo conditionalInfo() {
        return conditionalInfo;
    }

    public PaymentData paymentData() {
        return paymentData;
    }

    public String paymentID() {
        return paymentID;
    }

    public String categoryName() {
        return categoryName;
    }

    public String getAppLink() {
        return appLink;
    }

    public String getUpstream() {
        return upstream;
    }

    public String category() {
        return category;
    }

    public String id() {
        return id;
    }

    public String createdAt() {
        return createdAt;
    }

    public int status() {
        return status;
    }

    public String statusStr() {
        return statusStr;
    }

    public String statusColor() {
        return statusColor;
    }

    public String invoiceRefNum() {
        return invoiceRefNum;
    }

    public String title() {
        return title;
    }

    public List<MetaData> metaData() {
        return metaData;
    }

    public List<DotMenuList> dotMenuList() {
        return dotMenuList;
    }

    public List<DotMenuList> dotMenu() {
        return dotMenu;
    }

    public List<ActionButton> actionButtons() {
        return actionButtons;
    }

    public String totalInvoices() {
        return totalInvoices;
    }

    public List<Item> items() {
        return items;
    }

    public String getItemCount() {
        return itemCount;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {

        return "[ conditionalInfo : { " + conditionalInfo + " } " +
                ", paymentData : " + paymentData +
                ", paymentID : " + paymentID +
                ", status : " + status +
                ", statusColor : " + statusColor +
                ", statusStr : " + statusStr +
                ", metaData : " + metaData +
                ", actionButtons : " + actionButtons +
                "]";
    }

}
