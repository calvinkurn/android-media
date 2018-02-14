package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.transaction.checkout.di.scope.ShipmentAddressListScope;
import com.tokopedia.transaction.checkout.view.ShipmentAddressListFragment;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentAddressListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */
@Module
public class ShipmentAddressListModule {

    private final ShipmentAddressListAdapter.ActionListener actionListener;

    public ShipmentAddressListModule(ShipmentAddressListFragment shipmentAddressListFragment) {
        actionListener = shipmentAddressListFragment;
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListPresenter provideCartAddressListPresenter() {
        return new ShipmentAddressListPresenter();
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter(actionListener);
    }


}
