package com.tokopedia.seller.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityItemViewModel implements Parcelable {

    private int orderReplacementId;
    private int orderOrderId;
    private String orderPaymentAt;
    private String orderExpiredAt;
    private String orderCashbackIdr;
    private String orderCashback;
    private OrderCustomerViewModel orderCustomer;
    private OrderPaymentViewModel orderPayment;
    private OrderDetailViewModel orderDetail;
    private OrderDeadlineViewModel orderDeadline;
    private OrderShopViewModel orderShop;
    private List<OrderProductViewModel> orderProducts;
    private OrderShipmentViewModel orderShipment;
    private OrderLastViewModel orderLast;
    private List<OrderHistoryViewModel> orderHistory;
    private OrderDestinationViewModel orderDestination;
    private int position;
    private int replacementMultiplierValue;
    private String replacementMultiplierColor;
    private String replacementMultiplierText;

    // replacement URL for webview to show the terms and condition before the product being taken
    private String replacementTnc;

    public OpportunityItemViewModel() {
    }

    public int getOrderReplacementId() {
        return orderReplacementId;
    }

    public void setOrderReplacementId(int orderReplacementId) {
        this.orderReplacementId = orderReplacementId;
    }

    public int getOrderOrderId() {
        return orderOrderId;
    }

    public void setOrderOrderId(int orderOrderId) {
        this.orderOrderId = orderOrderId;
    }

    public String getOrderPaymentAt() {
        return orderPaymentAt;
    }

    public void setOrderPaymentAt(String orderPaymentAt) {
        this.orderPaymentAt = orderPaymentAt;
    }

    public String getOrderExpiredAt() {
        return orderExpiredAt;
    }

    public void setOrderExpiredAt(String orderExpiredAt) {
        this.orderExpiredAt = orderExpiredAt;
    }

    public String getOrderCashbackIdr() {
        return orderCashbackIdr;
    }

    public void setOrderCashbackIdr(String orderCashbackIdr) {
        this.orderCashbackIdr = orderCashbackIdr;
    }

    public String getOrderCashback() {
        return orderCashback;
    }

    public void setOrderCashback(String orderCashback) {
        this.orderCashback = orderCashback;
    }

    public OrderCustomerViewModel getOrderCustomer() {
        return orderCustomer;
    }

    public void setOrderCustomer(OrderCustomerViewModel orderCustomer) {
        this.orderCustomer = orderCustomer;
    }

    public OrderPaymentViewModel getOrderPayment() {
        return orderPayment;
    }

    public void setOrderPayment(OrderPaymentViewModel orderPayment) {
        this.orderPayment = orderPayment;
    }

    public OrderDetailViewModel getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(OrderDetailViewModel orderDetail) {
        this.orderDetail = orderDetail;
    }

    public OrderDeadlineViewModel getOrderDeadline() {
        return orderDeadline;
    }

    public void setOrderDeadline(OrderDeadlineViewModel orderDeadline) {
        this.orderDeadline = orderDeadline;
    }

    public OrderShopViewModel getOrderShop() {
        return orderShop;
    }

    public void setOrderShop(OrderShopViewModel orderShop) {
        this.orderShop = orderShop;
    }

    public List<OrderProductViewModel> getOrderProducts() {
        return orderProducts;
    }

    public void setOrderProducts(List<OrderProductViewModel> orderProducts) {
        this.orderProducts = orderProducts;
    }

    public OrderShipmentViewModel getOrderShipment() {
        return orderShipment;
    }

    public void setOrderShipment(OrderShipmentViewModel orderShipment) {
        this.orderShipment = orderShipment;
    }

    public OrderLastViewModel getOrderLast() {
        return orderLast;
    }

    public void setOrderLast(OrderLastViewModel orderLast) {
        this.orderLast = orderLast;
    }

    public List<OrderHistoryViewModel> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistoryViewModel> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public OrderDestinationViewModel getOrderDestination() {
        return orderDestination;
    }

    public void setOrderDestination(OrderDestinationViewModel orderDestination) {
        this.orderDestination = orderDestination;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public int getReplacementMultiplierValue() {
        return replacementMultiplierValue;
    }

    public void setReplacementMultiplierValue(int replacementMultiplierValue) {
        this.replacementMultiplierValue = replacementMultiplierValue;
    }

    public String getReplacementMultiplierColor() {
        return replacementMultiplierColor;
    }

    public void setReplacementMultiplierColor(String replacementMultiplierColor) {
        this.replacementMultiplierColor = replacementMultiplierColor;
    }


    public String getReplacementMultiplierText() {
        return replacementMultiplierText;
    }

    public void setReplacementMultiplierText(String replacementMultiplierText) {
        this.replacementMultiplierText = replacementMultiplierText;
    }

    public String getReplacementTnc() {
        return replacementTnc;
    }

    public void setReplacementTnc(String replacementTnc) {
        this.replacementTnc = replacementTnc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.orderReplacementId);
        dest.writeInt(this.orderOrderId);
        dest.writeString(this.orderPaymentAt);
        dest.writeString(this.orderExpiredAt);
        dest.writeString(this.orderCashbackIdr);
        dest.writeString(this.orderCashback);
        dest.writeParcelable(this.orderCustomer, flags);
        dest.writeParcelable(this.orderPayment, flags);
        dest.writeParcelable(this.orderDetail, flags);
        dest.writeParcelable(this.orderDeadline, flags);
        dest.writeParcelable(this.orderShop, flags);
        dest.writeTypedList(this.orderProducts);
        dest.writeParcelable(this.orderShipment, flags);
        dest.writeParcelable(this.orderLast, flags);
        dest.writeTypedList(this.orderHistory);
        dest.writeParcelable(this.orderDestination, flags);
        dest.writeInt(this.position);
        dest.writeInt(this.replacementMultiplierValue);
        dest.writeString(this.replacementMultiplierColor);
        dest.writeString(this.replacementMultiplierText);
        dest.writeString(this.replacementTnc);
    }

    protected OpportunityItemViewModel(Parcel in) {
        this.orderReplacementId = in.readInt();
        this.orderOrderId = in.readInt();
        this.orderPaymentAt = in.readString();
        this.orderExpiredAt = in.readString();
        this.orderCashbackIdr = in.readString();
        this.orderCashback = in.readString();
        this.orderCustomer = in.readParcelable(OrderCustomerViewModel.class.getClassLoader());
        this.orderPayment = in.readParcelable(OrderPaymentViewModel.class.getClassLoader());
        this.orderDetail = in.readParcelable(OrderDetailViewModel.class.getClassLoader());
        this.orderDeadline = in.readParcelable(OrderDeadlineViewModel.class.getClassLoader());
        this.orderShop = in.readParcelable(OrderShopViewModel.class.getClassLoader());
        this.orderProducts = in.createTypedArrayList(OrderProductViewModel.CREATOR);
        this.orderShipment = in.readParcelable(OrderShipmentViewModel.class.getClassLoader());
        this.orderLast = in.readParcelable(OrderLastViewModel.class.getClassLoader());
        this.orderHistory = in.createTypedArrayList(OrderHistoryViewModel.CREATOR);
        this.orderDestination = in.readParcelable(OrderDestinationViewModel.class.getClassLoader());
        this.position = in.readInt();
        this.replacementMultiplierValue = in.readInt();
        this.replacementMultiplierColor = in.readString();
        this.replacementMultiplierText = in.readString();
        this.replacementTnc = in.readString();
    }

    public static final Parcelable.Creator<OpportunityItemViewModel> CREATOR = new Parcelable.Creator<OpportunityItemViewModel>() {
        @Override
        public OpportunityItemViewModel createFromParcel(Parcel source) {
            return new OpportunityItemViewModel(source);
        }

        @Override
        public OpportunityItemViewModel[] newArray(int size) {
            return new OpportunityItemViewModel[size];
        }
    };
}
