package com.tokopedia.inbox.rescenter.inboxv2.view.di;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.inbox.rescenter.inboxv2.data.factory.ResoInboxFactory;
import com.tokopedia.inbox.rescenter.inboxv2.data.mapper.GetInboxMapper;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxBuyerUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxSellerUseCase;

import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by yfsx on 24/01/18.
 */

public class ResoInboxModule {
    @ResoInboxScope
    @Provides
    ResolutionApi provideResolutionService(@ResolutionQualifier Retrofit retrofit) {
        return retrofit.create(ResolutionApi.class);
    }

    @ResoInboxScope
    @Provides
    GetInboxMapper provideGetInboxMapper() {
        return new GetInboxMapper();
    }

    @ResoInboxScope
    @Provides
    ResoInboxFactory provideResoInboxFactory(
            ResolutionApi resolutionApi,
            GetInboxMapper getInboxMapper) {
        return new ResoInboxFactory(resolutionApi, getInboxMapper);
    }

    @ResoInboxScope
    @Provides
    GetInboxBuyerUseCase provideGetInboxUseCase(ResoInboxFactory factory) {
        return new GetInboxBuyerUseCase(factory);
    }

    @ResoInboxScope
    @Provides
    GetInboxSellerUseCase provideGetInboxSellerUseCase(ResoInboxFactory factory) {
        return new GetInboxSellerUseCase(factory);
    }
}
