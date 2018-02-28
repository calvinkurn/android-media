package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.module.CartAddressChoiceModule;
import com.tokopedia.transaction.checkout.di.scope.CartAddressChoiceScope;
import com.tokopedia.transaction.checkout.view.view.addressoptions.CartAddressChoiceFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 27/02/18
 */

@CartAddressChoiceScope
@Component(modules = CartAddressChoiceModule.class)
public interface CartAddressChoiceComponent {

    void inject(CartAddressChoiceFragment cartAddressChoiceFragment);

}
