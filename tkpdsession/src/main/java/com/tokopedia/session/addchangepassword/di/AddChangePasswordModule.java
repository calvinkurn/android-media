package com.tokopedia.session.addchangepassword.di;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.network.service.AccountsService;
import com.tokopedia.session.addchangepassword.data.mapper.AddPasswordMapper;
import com.tokopedia.session.addchangepassword.data.source.AddPasswordSource;
import com.tokopedia.session.addchangepassword.domain.usecase.AddPasswordUseCase;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

import static com.tokopedia.di.UserModule.BEARER_SERVICE;


/**
 * @author by yfsx on 09/04/18.
 */
@Module
public class AddChangePasswordModule {

    @AddChangePasswordScope
    @Provides
    AddPasswordSource provideAddPasswordSource(@Named(BEARER_SERVICE) AccountsService service,
                                               AddPasswordMapper addPasswordMapper,
                                               SessionHandler sessionHandler) {
        return new AddPasswordSource(service, addPasswordMapper, sessionHandler);
    }

    @AddChangePasswordScope
    @Provides
    AddPasswordUseCase provideAddPasswordUseCase(AddPasswordSource source) {
        return new AddPasswordUseCase(source);
    }
}
