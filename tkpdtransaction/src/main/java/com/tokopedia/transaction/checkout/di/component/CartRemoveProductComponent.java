package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.module.CartRemoveProductModule;
import com.tokopedia.transaction.checkout.di.scope.CartRemoveProductScope;
import com.tokopedia.transaction.checkout.view.CartRemoveProductFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@CartRemoveProductScope
@Component(modules = CartRemoveProductModule.class)
public interface CartRemoveProductComponent {
    void inject(CartRemoveProductFragment cartRemoveProductFragment);
}
