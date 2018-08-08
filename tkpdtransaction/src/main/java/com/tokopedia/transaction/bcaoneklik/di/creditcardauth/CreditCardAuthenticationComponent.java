package com.tokopedia.transaction.bcaoneklik.di.creditcardauth;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.transaction.bcaoneklik.activity.CreditCardAuthenticationActivity;
import com.tokopedia.transaction.bcaoneklik.di.PaymentOptionScope;

import dagger.Component;

/**
 * Created by kris on 10/11/17. Tokopedia
 */

@CreditCardAuthenticationScope
@Component(modules = CreditCardAuthenticationModule.class, dependencies = AppComponent.class)
public interface CreditCardAuthenticationComponent {
    void inject(CreditCardAuthenticationActivity activity);
}
