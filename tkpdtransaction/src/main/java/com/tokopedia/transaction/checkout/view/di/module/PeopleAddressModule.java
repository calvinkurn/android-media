package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.transaction.checkout.data.repository.NonPaginatedPeopleAddressRepositoryImpl;
import com.tokopedia.transaction.checkout.data.repository.PaginatedPeopleAddressRepositoryImpl;
import com.tokopedia.transaction.checkout.data.repository.PeopleAddressRepository;
import com.tokopedia.transaction.checkout.view.di.qualifier.NonPaginatedAddressQualifier;
import com.tokopedia.transaction.checkout.view.di.qualifier.PaginatedAddressQualifier;

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
