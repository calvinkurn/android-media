package com.tokopedia.transaction.bcaoneklik.di.fingerprint;

import android.app.Activity;

import com.tokopedia.transaction.bcaoneklik.usecase.CreditCardFingerPrintUseCase;

import dagger.Component;

/**
 * Created by kris on 4/24/18. Tokopedia
 */

@SingleAuthenticationScope
@Component(modules = SingleAuthenticationModule.class)
public interface SingleAuthenticationComponent {
    void inject(CreditCardFingerPrintUseCase useCase);
}
