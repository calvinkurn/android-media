package com.tokopedia.seller.opportunity.viewmodel.opportunitylist;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityItemViewModel implements Parcelable{

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

    public OpportunityItemViewModel() {
    }

    protected OpportunityItemViewModel(Parcel in) {
        orderReplacementId = in.readInt();
        orderOrderId = in.readInt();
        orderPaymentAt = in.readString();
        orderExpiredAt = in.readString();
        orderCashbackIdr = in.readString();
        orderCashback = in.readString();
        orderCustomer = in.readParcelable(OrderCustomerViewModel.class.getClassLoader());
        orderPayment = in.readParcelable(OrderPaymentViewModel.class.getClassLoader());
        orderDetail = in.readParcelable(OrderDetailViewModel.class.getClassLoader());
        orderDeadline = in.readParcelable(OrderDeadlineViewModel.class.getClassLoader());
        orderShop = in.readParcelable(OrderShopViewModel.class.getClassLoader());
        orderProducts = in.createTypedArrayList(OrderProductViewModel.CREATOR);
        orderShipment = in.readParcelable(OrderShipmentViewModel.class.getClassLoader());
        orderLast = in.readParcelable(OrderLastViewModel.class.getClassLoader());
        orderHistory = in.createTypedArrayList(OrderHistoryViewModel.CREATOR);
        orderDestination = in.readParcelable(OrderDestinationViewModel.class.getClassLoader());
        position = in.readInt();
        replacementMultiplierValue = in.readInt();
        replacementMultiplierColor = in.readString();
        replacementMultiplierText = in.readString();
    }

    public static final Creator<OpportunityItemViewModel> CREATOR = new Creator<OpportunityItemViewModel>() {
        @Override
        public OpportunityItemViewModel createFromParcel(Parcel in) {
            return new OpportunityItemViewModel(in);
        }

        @Override
        public OpportunityItemViewModel[] newArray(int size) {
            return new OpportunityItemViewModel[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(orderReplacementId);
        dest.writeInt(orderOrderId);
        dest.writeString(orderPaymentAt);
        dest.writeString(orderExpiredAt);
        dest.writeString(orderCashbackIdr);
        dest.writeString(orderCashback);
        dest.writeParcelable(orderCustomer, flags);
        dest.writeParcelable(orderPayment, flags);
        dest.writeParcelable(orderDetail, flags);
        dest.writeParcelable(orderDeadline, flags);
        dest.writeParcelable(orderShop, flags);
        dest.writeTypedList(orderProducts);
        dest.writeParcelable(orderShipment, flags);
        dest.writeParcelable(orderLast, flags);
        dest.writeTypedList(orderHistory);
        dest.writeParcelable(orderDestination, flags);
        dest.writeInt(position);
        dest.writeInt(replacementMultiplierValue);
        dest.writeString(replacementMultiplierColor);
        dest.writeString(replacementMultiplierText);
    }
}
