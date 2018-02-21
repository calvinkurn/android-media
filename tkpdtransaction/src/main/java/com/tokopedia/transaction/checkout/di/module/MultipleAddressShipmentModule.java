package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.MultipleAddressShipmentScope;
import com.tokopedia.transaction.checkout.view.view.shipmentform.MultipleAddressShipmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by kris on 2/5/18. Tokopedia
 */

@Module(includes = {DataModule.class})
public class MultipleAddressShipmentModule {

    @MultipleAddressShipmentScope
    @Provides
    MultipleAddressShipmentPresenter providePresenter() {
        return new MultipleAddressShipmentPresenter();
    }

}
