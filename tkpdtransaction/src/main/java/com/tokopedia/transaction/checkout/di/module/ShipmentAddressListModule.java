package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.transaction.checkout.data.repository.PeopleAddressRepositoryImpl;
import com.tokopedia.transaction.checkout.di.scope.ShipmentAddressListScope;
import com.tokopedia.transaction.checkout.domain.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.domain.usecase.GetAddressListUseCase;
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
    ShipmentAddressListPresenter provideCartAddressListPresenter(GetAddressListUseCase getAddressListUseCase) {
        return new ShipmentAddressListPresenter(getAddressListUseCase);
    }

    @Provides
    @ShipmentAddressListScope
    ShipmentAddressListAdapter provideCartAddressListAdapter() {
        return new ShipmentAddressListAdapter(actionListener);
    }

    @Provides
    @ShipmentAddressListScope
    PeopleService providePeopleService() {
        return new PeopleService();
    }

    @Provides
    @ShipmentAddressListScope
    PeopleAddressRepositoryImpl providePeopleAddressRepositoryImpl(PeopleService peopleService) {
        return new PeopleAddressRepositoryImpl(peopleService);
    }

    @Provides
    @ShipmentAddressListScope
    PeopleAddressRepository providePeopleAddressRepository(PeopleAddressRepositoryImpl peopleAddressRepositoryImpl) {
        return peopleAddressRepositoryImpl;
    }

    @Provides
    @ShipmentAddressListScope
    GetAddressListUseCase provideGetAddressListUseCase(PeopleAddressRepository peopleAddressRepository) {
        return new GetAddressListUseCase(peopleAddressRepository);
    }

}
