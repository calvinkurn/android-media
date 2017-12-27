package com.tokopedia.transaction.purchase.model.response.txlist;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tokopedia.core.purchase.model.response.txlist.OrderHistory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Angga.Prasetiyo on 21/04/2016.
 */
public class OrderData implements Parcelable{
    private static final String TAG = OrderData.class.getSimpleName();

    @SerializedName("order_JOB_status")
    @Expose
    private Object orderJOBStatus;
    @SerializedName("order_detail")
    @Expose
    private OrderDetail orderDetail;
    @SerializedName("order_auto_resi")
    @Expose
    private String orderAutoResi;
    @SerializedName("order_deadline")
    @Expose
    private OrderDeadline orderDeadline;
    @SerializedName("order_auto_awb")
    @Expose
    private String orderAutoAwb;
    @SerializedName("order_button")
    @Expose
    private OrderButton orderButton;
    @SerializedName("driver_info")
    @Expose
    private OrderDriver driverInfo;
    @SerializedName("order_products")
    @Expose
    private List<OrderProduct> orderProducts = new ArrayList<OrderProduct>();
    @SerializedName("order_shop")
    @Expose
    private OrderShop orderShop;
    @SerializedName("order_shipment")
    @Expose
    private OrderShipment orderShipment;
    @SerializedName("order_last")
    @Expose
    private OrderLast orderLast;
    @SerializedName("order_history")
    @Expose
    private List<OrderHistory> orderHistory = new ArrayList<OrderHistory>();
    @SerializedName("order_JOB_detail")
    @Expose
    private Object orderJOBDetail;
    @SerializedName("order_destination")
    @Expose
    private OrderDestination orderDestination;

    protected OrderData(Parcel in) {
        orderDetail = in.readParcelable(OrderDetail.class.getClassLoader());
        orderAutoResi = in.readString();
        orderDeadline = in.readParcelable(OrderDeadline.class.getClassLoader());
        orderAutoAwb = in.readString();
        orderButton = in.readParcelable(OrderButton.class.getClassLoader());
        driverInfo = in.readParcelable(OrderDriver.class.getClassLoader());
        orderProducts = in.createTypedArrayList(OrderProduct.CREATOR);
        orderShop = in.readParcelable(OrderShop.class.getClassLoader());
        orderShipment = in.readParcelable(OrderShipment.class.getClassLoader());
        orderLast = in.readParcelable(OrderLast.class.getClassLoader());
        orderHistory = in.createTypedArrayList(OrderHistory.CREATOR);
        orderDestination = in.readParcelable(OrderDestination.class.getClassLoader());
    }

    public static final Creator<OrderData> CREATOR = new Creator<OrderData>() {
        @Override
        public OrderData createFromParcel(Parcel in) {
            return new OrderData(in);
        }

        @Override
        public OrderData[] newArray(int size) {
            return new OrderData[size];
        }
    };

    public Object getOrderJOBStatus() {
        return orderJOBStatus;
    }

    public void setOrderJOBStatus(Object orderJOBStatus) {
        this.orderJOBStatus = orderJOBStatus;
    }

    public OrderDetail getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetail orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrderAutoResi() {
        return orderAutoResi;
    }

    public void setOrderAutoResi(String orderAutoResi) {
        this.orderAutoResi = orderAutoResi;
    }

    public OrderDeadline getOrderDeadline() {
        return orderDeadline;
    }

    public void setOrderDeadline(OrderDeadline orderDeadline) {
        this.orderDeadline = orderDeadline;
    }

    public String getOrderAutoAwb() {
        return orderAutoAwb;
    }

    public void setOrderAutoAwb(String orderAutoAwb) {
        this.orderAutoAwb = orderAutoAwb;
    }

    public OrderButton getOrderButton() {
        return orderButton;
    }

    public void setOrderButton(OrderButton orderButton) {
        this.orderButton = orderButton;
    }

    public OrderDriver getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(OrderDriver driverInfo) {
        this.driverInfo = driverInfo;
    }

    public List<OrderProduct> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProduct> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public OrderShop getOrderShop() {
        return orderShop;
    }

    public void setOrderShop(OrderShop orderShop) {
        this.orderShop = orderShop;
    }

    public OrderShipment getOrderShipment() {
        return orderShipment;
    }

    public void setOrderShipment(OrderShipment orderShipment) {
        this.orderShipment = orderShipment;
    }

    public OrderLast getOrderLast() {
        return orderLast;
    }

    public void setOrderLast(OrderLast orderLast) {
        this.orderLast = orderLast;
    }

    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public Object getOrderJOBDetail() {
        return orderJOBDetail;
    }

    public void setOrderJOBDetail(Object orderJOBDetail) {
        this.orderJOBDetail = orderJOBDetail;
    }

    public OrderDestination getOrderDestination() {
        return orderDestination;
    }

    public void setOrderDestination(OrderDestination orderDestination) {
        this.orderDestination = orderDestination;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(orderDetail, flags);
        dest.writeString(orderAutoResi);
        dest.writeParcelable(orderDeadline, flags);
        dest.writeString(orderAutoAwb);
        dest.writeParcelable(orderButton, flags);
        dest.writeParcelable(driverInfo, flags);
        dest.writeTypedList(orderProducts);
        dest.writeParcelable(orderShop, flags);
        dest.writeParcelable(orderShipment, flags);
        dest.writeParcelable(orderLast, flags);
        dest.writeTypedList(orderHistory);
        dest.writeParcelable(orderDestination, flags);
    }
}
