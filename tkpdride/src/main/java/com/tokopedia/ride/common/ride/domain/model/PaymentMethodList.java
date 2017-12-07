
package com.tokopedia.ride.common.ride.domain.model;

import java.util.List;

public class PaymentMethodList {

    private List<PaymentMethod> paymentMethods = null;
    private AddPayment addPayment;

    public List<PaymentMethod> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethod> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public AddPayment getAddPayment() {
        return addPayment;
    }

    public void setAddPayment(AddPayment addPayment) {
        this.addPayment = addPayment;
    }

}
