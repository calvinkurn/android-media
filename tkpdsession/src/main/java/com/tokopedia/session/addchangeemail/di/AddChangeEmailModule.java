package com.tokopedia.session.addchangeemail.di;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.addchangeemail.data.mapper.AddEmailMapper;
import com.tokopedia.session.addchangeemail.data.mapper.CheckEmailMapper;
import com.tokopedia.session.addchangeemail.data.mapper.RequestVerificationMapper;
import com.tokopedia.session.addchangeemail.data.source.AddEmailSource;
import com.tokopedia.session.addchangeemail.domain.usecase.AddEmailUseCase;
import com.tokopedia.session.addchangeemail.domain.usecase.CheckEmailUseCase;
import com.tokopedia.session.addchangeemail.domain.usecase.RequestVerificationUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.di.UserModule.BEARER_SERVICE;


/**
 * @author by yfsx on 09/04/18.
 */
@Module
public class AddChangeEmailModule {

    @AddChangeEmailScope
    @Provides
    AddEmailSource provideAddEmailSource(@Named(BEARER_SERVICE) AccountsService service,
                                         AddEmailMapper addEmailMapper,
                                         CheckEmailMapper checkEmailMapper,
                                         RequestVerificationMapper requestVerificationMapper,
                                         GlobalCacheManager cacheManager) {
        return new AddEmailSource(service, addEmailMapper, checkEmailMapper, requestVerificationMapper, cacheManager);
    }

    @AddChangeEmailScope
    @Provides
    RequestVerificationUseCase provideRequestVerificationUseCase(AddEmailSource source) {
        return new RequestVerificationUseCase(source);
    }

    @AddChangeEmailScope
    @Provides
    CheckEmailUseCase provideCheckEmailUseCase(AddEmailSource source) {
        return new CheckEmailUseCase(source);
    }

    @AddChangeEmailScope
    @Provides
    AddEmailUseCase provideAddEmailUseCase(AddEmailSource source) {
        return new AddEmailUseCase(source);
    }
}
