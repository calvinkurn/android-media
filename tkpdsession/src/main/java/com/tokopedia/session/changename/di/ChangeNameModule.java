package com.tokopedia.session.changename.di;

import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.changename.data.mapper.ChangeNameMapper;
import com.tokopedia.session.changename.data.source.ChangeNameSource;
import com.tokopedia.session.changename.domain.usecase.ChangeNameUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.di.UserModule.BEARER_SERVICE;


/**
 * @author by yfsx on 09/04/18.
 */
@Module
public class ChangeNameModule {

    @ChangeNameScope
    @Provides
    ChangeNameSource provideChangeNameSource(@Named(BEARER_SERVICE) AccountsService service,
                                             ChangeNameMapper changeNameMapper,
                                             GlobalCacheManager cacheManager) {
        return new ChangeNameSource(service, changeNameMapper, cacheManager);
    }

    @ChangeNameScope
    @Provides
    ChangeNameUseCase provideChangeNameUseCase(ChangeNameSource source) {
        return new ChangeNameUseCase(source);
    }
}
