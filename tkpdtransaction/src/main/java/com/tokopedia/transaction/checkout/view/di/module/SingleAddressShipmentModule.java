package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.di.scope.SingleAddressShipmentScope;
import com.tokopedia.transaction.checkout.view.view.shipmentform.SingleAddressShipmentFragment;
import com.tokopedia.transaction.checkout.view.view.shipmentform.SingleAddressShipmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {DataModule.class, ConverterDataModule.class, PeopleAddressModule.class})
public class SingleAddressShipmentModule {

    public SingleAddressShipmentModule(SingleAddressShipmentFragment singleAddressShipmentFragment) {

    }

    @Provides
    @SingleAddressShipmentScope
    SingleAddressShipmentPresenter provideCartSingleAddressPresenter() {
        return new SingleAddressShipmentPresenter();
    }

    @Provides
    @SingleAddressShipmentScope
    SingleAddressShipmentAdapter provideCartSingleAddressAdapter() {
        return new SingleAddressShipmentAdapter();
    }

}
