package com.tokopedia.transaction.checkout.view.di.component;

import com.tokopedia.transaction.checkout.view.di.module.DataModule;
import com.tokopedia.transaction.checkout.view.di.scope.CartListScope;

import dagger.Component;

/**
 * @author anggaprasetiyo on 13/02/18.
 */
@CartListScope
@Component(modules = DataModule.class)
public interface AddToCartComponent {
}
