
package com.tokopedia.ride.common.ride.data.entity;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PaymentMethodListEntity {

    @SerializedName("payment_methods")
    @Expose
    private List<PaymentMethodEntity> paymentMethods = null;
    @SerializedName("add_payment")
    @Expose
    private AddPaymentEntity addPayment;

    public List<PaymentMethodEntity> getPaymentMethods() {
        return paymentMethods;
    }

    public void setPaymentMethods(List<PaymentMethodEntity> paymentMethods) {
        this.paymentMethods = paymentMethods;
    }

    public AddPaymentEntity getAddPayment() {
        return addPayment;
    }

    public void setAddPayment(AddPaymentEntity addPayment) {
        this.addPayment = addPayment;
    }

}
