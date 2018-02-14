package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.module.CartListModule;
import com.tokopedia.transaction.checkout.di.module.DataModule;
import com.tokopedia.transaction.checkout.di.scope.CartListScope;

import dagger.Component;

/**
 * @author anggaprasetiyo on 13/02/18.
 */
@CartListScope
@Component(modules = DataModule.class)
public interface AddToCartComponent {
}
