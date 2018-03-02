package com.tokopedia.transaction.checkout.view.di.component;

import com.tokopedia.transaction.checkout.view.di.module.AddShipmentAddressModule;
import com.tokopedia.transaction.checkout.view.di.scope.AddShipmentAddressScope;
import com.tokopedia.transaction.checkout.view.view.multipleaddressform.AddShipmentAddressActivity;

import dagger.Component;

/**
 * Created by kris on 3/1/18. Tokopedia
 */

@AddShipmentAddressScope
@Component(modules = AddShipmentAddressModule.class)
public interface AddShipmentAddressComponent {
    void inject(AddShipmentAddressActivity activity);
}
