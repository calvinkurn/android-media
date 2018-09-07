package com.tokopedia.transaction.bcaoneklik.di;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.transaction.bcaoneklik.activity.CreditCardDetailActivity;
import com.tokopedia.transaction.bcaoneklik.activity.ListPaymentTypeActivity;

import dagger.Component;

/**
 * Created by kris on 8/21/17. Tokopedia
 */

@PaymentOptionScope
@Component(modules = PaymentOptionModule.class, dependencies = AppComponent.class)
public interface PaymentOptionComponent {
    void inject(ListPaymentTypeActivity activity);

    void inject(CreditCardDetailActivity activity);

}
