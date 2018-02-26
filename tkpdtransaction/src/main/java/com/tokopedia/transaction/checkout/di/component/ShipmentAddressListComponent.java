package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.module.ShipmentAddressListModule;
import com.tokopedia.transaction.checkout.di.scope.ShipmentAddressListScope;
import com.tokopedia.transaction.checkout.view.view.addressoptions.ShipmentAddressListFragment;

import dagger.Component;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@ShipmentAddressListScope
@Component(modules = ShipmentAddressListModule.class)
public interface ShipmentAddressListComponent {
    void inject(ShipmentAddressListFragment shipmentAddressListFragment);
}
