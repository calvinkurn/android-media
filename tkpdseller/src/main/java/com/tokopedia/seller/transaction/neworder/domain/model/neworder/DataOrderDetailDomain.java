
package com.tokopedia.seller.transaction.neworder.domain.model.neworder;

public class DataOrderDetailDomain {
    private int paymentProcessDayLeft;
    private int orderId;
    private String detailOrderDate;
    private String customerName;

    public int getPaymentProcessDayLeft() {
        return paymentProcessDayLeft;
    }

    public void setPaymentProcessDayLeft(int paymentProcessDayLeft) {
        this.paymentProcessDayLeft = paymentProcessDayLeft;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDetailOrderDate() {
        return detailOrderDate;
    }

    public void setDetailOrderDate(String detailOrderDate) {
        this.detailOrderDate = detailOrderDate;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
