package com.tokopedia.transaction.others;

import dagger.Component;

@SingleAuthenticationScope
@Component(modules = SingleAuthenticationModule.class)
public interface SingleAuthenticationComponent {
    void inject(CreditCardFingerPrintUseCase useCase);
}