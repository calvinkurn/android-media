package com.tokopedia.transaction.checkout.view.di.component;

import com.tokopedia.transaction.checkout.view.di.module.SingleAddressShipmentModule;
import com.tokopedia.transaction.checkout.view.di.scope.SingleAddressShipmentScope;
import com.tokopedia.transaction.checkout.view.view.shipmentform.SingleAddressShipmentFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@SingleAddressShipmentScope
@Component(modules = SingleAddressShipmentModule.class)
public interface SingleAddressShipmentComponent {

    void inject(SingleAddressShipmentFragment singleAddressShipmentFragment);

}
