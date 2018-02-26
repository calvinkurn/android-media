package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.checkout.di.qualifier.PaginatedAddressQualifier;
import com.tokopedia.transaction.checkout.di.scope.ShipmentAddressListScope;
import com.tokopedia.transaction.checkout.domain.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.domain.usecase.GetAllAddressUseCase;
import com.tokopedia.transaction.checkout.view.view.addressoptions.ShipmentAddressListFragment;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentAddressListPresenter;

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
    ShipmentAddressListPresenter provideCartAddressListPresenter(GetAllAddressUseCase getAllAddressUseCase, PagingHandler pagingHandler) {
        return new ShipmentAddressListPresenter(getAllAddressUseCase, pagingHandler);
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter(actionListener);
    }

    @Provides
    @ShipmentAddressListScope
    GetAllAddressUseCase provideGetAddressListUseCase(@PaginatedAddressQualifier PeopleAddressRepository peopleAddressRepository) {
        return new GetAllAddressUseCase(peopleAddressRepository);
    }

}