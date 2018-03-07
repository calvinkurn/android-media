package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.domain.usecase.GetPeopleAddressUseCase;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.di.scope.ShipmentAddressListScope;
import com.tokopedia.transaction.checkout.view.view.addressoptions.ShipmentAddressListFragment;
import com.tokopedia.transaction.checkout.view.view.addressoptions.ShipmentAddressListPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module(includes = {PeopleAddressModule.class})
public class ShipmentAddressListModule {

    private final ShipmentAddressListAdapter.ActionListener actionListener;

    public ShipmentAddressListModule(ShipmentAddressListFragment shipmentAddressListFragment) {
        actionListener = shipmentAddressListFragment;
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListPresenter provideCartAddressListPresenter(GetPeopleAddressUseCase getPeopleAddressUseCase, PagingHandler pagingHandler) {
        return new ShipmentAddressListPresenter(getPeopleAddressUseCase, pagingHandler);
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter(actionListener);
    }

    @Provides
    @ShipmentAddressListScope
    GetPeopleAddressUseCase provideGetAddressListUseCase(PeopleAddressRepository peopleAddressRepository) {
        return new GetPeopleAddressUseCase(peopleAddressRepository);
    }

}