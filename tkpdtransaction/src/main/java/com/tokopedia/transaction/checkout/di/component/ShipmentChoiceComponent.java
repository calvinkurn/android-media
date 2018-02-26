package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.module.ShipmentChoiceModule;
import com.tokopedia.transaction.checkout.di.scope.ShipmentChoiceScope;
import com.tokopedia.transaction.checkout.view.view.shippingoptions.ShipmentChoiceBottomSheet;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */

@ShipmentChoiceScope
@Component(modules = ShipmentChoiceModule.class)
public interface ShipmentChoiceComponent {
    void inject(ShipmentChoiceBottomSheet shipmentChoiceBottomSheet);
}