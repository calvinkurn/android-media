package com.tokopedia.transaction.checkout.di.component;

import com.tokopedia.transaction.checkout.di.scope.ShipmentDetailScope;
import com.tokopedia.transaction.checkout.view.ShipmentDetailFragment;

import dagger.Component;

/**
 * Created by Irfan Khoirul on 09/02/18.
 */

@ShipmentDetailScope
@Component(modules = ShipmentDetailComponent.class)
public interface ShipmentDetailComponent {
    void inject(ShipmentDetailFragment shipmentDetailFragment);
}
