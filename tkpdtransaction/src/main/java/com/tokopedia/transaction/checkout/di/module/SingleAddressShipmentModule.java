package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.SingleAddressShipmentScope;
import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.view.shipmentform.SingleAddressShipmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module(includes = {DataModule.class, ConverterDataModule.class})
public class SingleAddressShipmentModule {

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
