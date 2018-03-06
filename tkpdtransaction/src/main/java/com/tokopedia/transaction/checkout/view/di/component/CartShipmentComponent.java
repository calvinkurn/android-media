package com.tokopedia.transaction.checkout.view.di.component;

import com.tokopedia.transaction.checkout.view.di.module.CartShipmentModule;
import com.tokopedia.transaction.checkout.view.di.scope.CartShipmentActivityScope;
import com.tokopedia.transaction.checkout.view.view.shipmentform.CartShipmentActivity;

import dagger.Component;

/**
 * @author anggaprasetiyo on 05/03/18.
 */
@CartShipmentActivityScope
@Component(modules = CartShipmentModule.class)
public interface CartShipmentComponent {

    void inject(CartShipmentActivity cartShipmentActivity);
}
