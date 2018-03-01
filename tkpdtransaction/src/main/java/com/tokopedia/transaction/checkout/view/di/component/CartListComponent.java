package com.tokopedia.transaction.checkout.view.di.component;

import com.tokopedia.transaction.checkout.view.di.module.CartListModule;
import com.tokopedia.transaction.checkout.view.di.scope.CartListScope;
import com.tokopedia.transaction.checkout.view.view.cartlist.CartFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 18/01/18.
 */
@CartListScope
@Component(modules = CartListModule.class)
public interface CartListComponent {
    void inject(CartFragment cartFragment);
}
