package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.transaction.checkout.view.adapter.SingleAddressShipmentAdapter;
import com.tokopedia.transaction.checkout.view.di.scope.SingleAddressShipmentScope;
import com.tokopedia.transaction.checkout.view.mapper.ShipmentDataRequestConverter;
import com.tokopedia.transaction.checkout.view.view.shipmentform.ICartSingleAddressView;
import com.tokopedia.transaction.checkout.view.view.shipmentform.SingleAddressShipmentFragment;
import com.tokopedia.transaction.checkout.view.view.shipmentform.SingleAddressShipmentPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {DataModule.class, ConverterDataModule.class, PeopleAddressModule.class})
public class SingleAddressShipmentModule {

    private SingleAddressShipmentAdapter.ActionListener adapterActionListener;
    private ICartSingleAddressView viewListener;

    public SingleAddressShipmentModule(SingleAddressShipmentFragment singleAddressShipmentFragment) {
        adapterActionListener = singleAddressShipmentFragment;
        viewListener = singleAddressShipmentFragment;
    }

    @Provides
    @SingleAddressShipmentScope
    SingleAddressShipmentPresenter provideCartSingleAddressPresenter() {
        return new SingleAddressShipmentPresenter(viewListener);
    }

    @Provides
    @SingleAddressShipmentScope
    ShipmentDataRequestConverter provideShipmentDataRequestConverter() {
        return new ShipmentDataRequestConverter();
    }

    @Provides
    @SingleAddressShipmentScope
    SingleAddressShipmentAdapter provideCartSingleAddressAdapter(ShipmentDataRequestConverter shipmentDataRequestConverter) {
        return new SingleAddressShipmentAdapter(adapterActionListener, shipmentDataRequestConverter);
    }

}