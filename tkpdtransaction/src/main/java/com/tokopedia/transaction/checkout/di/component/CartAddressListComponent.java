package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.module.CartAddressListModule;
import com.tokopedia.transaction.checkout.di.scope.CartAddressListScope;
import com.tokopedia.transaction.checkout.view.ShippingAddressListFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@CartAddressListScope
@Component(modules = CartAddressListModule.class)
public interface CartAddressListComponent {
    void inject(ShippingAddressListFragment shippingAddressListFragment);
}
