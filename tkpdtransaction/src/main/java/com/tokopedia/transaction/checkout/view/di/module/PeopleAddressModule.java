package com.tokopedia.transaction.checkout.view.di.module;

import com.tokopedia.core.network.apiservices.user.PeopleService;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.transaction.checkout.data.mapper.AddressModelMapper;
import com.tokopedia.transaction.checkout.data.repository.PeopleAddressRepositoryImpl;
import com.tokopedia.transaction.checkout.data.repository.PeopleAddressRepository;

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
    PeopleAddressRepositoryImpl providePeopleAddressRepositoryImpl(PeopleService peopleService, AddressModelMapper addressModelMapper) {
        return new PeopleAddressRepositoryImpl(peopleService, addressModelMapper);
    }

    @Provides
    PeopleAddressRepository providePeopleAddressRepository(PeopleAddressRepositoryImpl peopleAddressRepositoryImpl) {
        return peopleAddressRepositoryImpl;
    }

}