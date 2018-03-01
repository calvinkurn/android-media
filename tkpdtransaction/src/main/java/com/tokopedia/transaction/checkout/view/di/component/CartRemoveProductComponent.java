package com.tokopedia.transaction.checkout.view.di.component;

import com.tokopedia.transaction.checkout.view.di.module.CartRemoveProductModule;
import com.tokopedia.transaction.checkout.view.di.scope.CartRemoveProductScope;
import com.tokopedia.transaction.checkout.view.view.cartlist.CartRemoveProductFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@CartRemoveProductScope
@Component(modules = CartRemoveProductModule.class)
public interface CartRemoveProductComponent {
    void inject(CartRemoveProductFragment cartRemoveProductFragment);
}
