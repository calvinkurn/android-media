package com.tokopedia.seller.opportunity.viewmodel.opportunitylist;

import java.util.List;

/**
 * Created by nisie on 3/1/17.
 */

public class OpportunityItemViewModel {

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
}
