package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.module.CartListModule;
import com.tokopedia.transaction.checkout.di.scope.CartListScope;
import com.tokopedia.transaction.checkout.view.CartFragment;

import dagger.Component;

/**
 * @author anggaprasetiyo on 18/01/18.
 */
@CartListScope
@Component(modules = CartListModule.class)
public interface CartListComponent {
    void inject(CartFragment cartFragment);
}
