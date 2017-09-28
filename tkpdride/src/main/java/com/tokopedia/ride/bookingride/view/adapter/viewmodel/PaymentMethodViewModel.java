package com.tokopedia.ride.bookingride.view.adapter.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.ride.bookingride.view.adapter.factory.PaymentMethodTypeFactory;

/**
 * Created by alvarisi on 5/3/17.
 */

public class PaymentMethodViewModel implements Visitable<PaymentMethodTypeFactory> {
    String name;
    boolean isActive;
    String type;

    public PaymentMethodViewModel(String name, boolean isActive, String type) {
        this.name = name;
        this.isActive = isActive;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int type(PaymentMethodTypeFactory paymentMethodTypeFactory) {
        return paymentMethodTypeFactory.type(this);
    }
}
