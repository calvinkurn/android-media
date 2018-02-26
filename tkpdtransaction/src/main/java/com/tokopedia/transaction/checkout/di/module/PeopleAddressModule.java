package com.tokopedia.transaction.checkout.di.module;

import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.transaction.checkout.data.repository.NonPaginatedPeopleAddressRepositoryImpl;
import com.tokopedia.transaction.checkout.data.repository.PaginatedPeopleAddressRepositoryImpl;
import com.tokopedia.transaction.checkout.di.qualifier.NonPaginatedAddressQualifier;
import com.tokopedia.transaction.checkout.di.qualifier.PaginatedAddressQualifier;
import com.tokopedia.transaction.checkout.di.scope.ShipmentAddressListScope;
import com.tokopedia.transaction.checkout.domain.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.domain.usecase.GetAllAddressUseCase;
import com.tokopedia.transaction.checkout.view.adapter.ShipmentAddressListAdapter;
import com.tokopedia.transaction.checkout.view.presenter.ShipmentAddressListPresenter;
import com.tokopedia.transaction.checkout.view.view.addressoptions.ShipmentAddressListFragment;

import dagger.Module;
import dagger.Provides;

/**
 * @author Aghny A. Putra on 31/01/18.
 */

@Module
public class PeopleAddressModule {

    @Provides
    PagingHandler providePagingHandler() {
        return new PagingHandler();
    }

    @Provides
    PeopleService providePeopleService() {
        return new PeopleService();
    }

    @Provides
    AddressModelMapper providePeopleAddressMapper() {
        return new AddressModelMapper();
    }

    @Provides
    PaginatedPeopleAddressRepositoryImpl providePeopleAddressRepositoryImpl(PeopleService peopleService, AddressModelMapper addressModelMapper) {
        return new PaginatedPeopleAddressRepositoryImpl(peopleService, addressModelMapper);
    }

    @Provides
    NonPaginatedPeopleAddressRepositoryImpl provideNonPeopleAddressRepositoryImpl(PeopleService peopleService, AddressModelMapper addressModelMapper) {
        return new NonPaginatedPeopleAddressRepositoryImpl(peopleService, addressModelMapper);
    }

    @Provides
    @PaginatedAddressQualifier
    PeopleAddressRepository providePaginatedPeopleAddressRepository(PaginatedPeopleAddressRepositoryImpl paginatedPeopleAddressRepositoryImpl) {
        return paginatedPeopleAddressRepositoryImpl;
    }

    @Provides
    @NonPaginatedAddressQualifier
    PeopleAddressRepository provideNonPaginatedPeopleAddressRepository(NonPaginatedPeopleAddressRepositoryImpl nonPaginatedPeopleAddressRepository) {
        return nonPaginatedPeopleAddressRepository;
    }

}
