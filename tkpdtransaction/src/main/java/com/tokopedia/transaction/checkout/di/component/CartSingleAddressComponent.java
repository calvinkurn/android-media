package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.module.CartSingleAddressModule;
import com.tokopedia.transaction.checkout.di.scope.CartSingleAddressScope;
import com.tokopedia.transaction.checkout.view.CartSingleAddressFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@CartSingleAddressScope
@Component(modules = CartSingleAddressModule.class)
public interface CartSingleAddressComponent {
    void inject(CartSingleAddressFragment cartSingleAddressFragment);
}
