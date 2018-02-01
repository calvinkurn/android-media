package com.tokopedia.inbox.rescenter.inboxv2.view.di;

import com.tokopedia.core.network.apiservices.rescenter.apis.ResolutionApi;
import com.tokopedia.core.network.di.qualifier.ResolutionQualifier;
import com.tokopedia.inbox.rescenter.inboxv2.data.mapper.GetInboxMapper;
import com.tokopedia.inbox.rescenter.inboxv2.data.mapper.GetInboxSingleItemMapper;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxBuyerLoadMoreUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxBuyerSingleItemUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxBuyerUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxSellerLoadMoreUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxSellerSingleItemUseCase;
import com.tokopedia.inbox.rescenter.inboxv2.domain.usecase.GetInboxSellerUseCase;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * Created by yfsx on 24/01/18.
 */

@Module
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
    GetInboxSingleItemMapper provideGetInboxSingleItemMapper() {
        return new GetInboxSingleItemMapper();
    }


    @ResoInboxScope
    @Provides
    GetInboxBuyerUseCase provideGetInboxUseCase(
            ResolutionApi resolutionApi, GetInboxMapper getInboxMapper) {
        return new GetInboxBuyerUseCase(resolutionApi, getInboxMapper);
    }

    @ResoInboxScope
    @Provides
    GetInboxSellerUseCase provideGetInboxSellerUseCase(
            ResolutionApi resolutionApi, GetInboxMapper getInboxMapper) {
        return new GetInboxSellerUseCase(resolutionApi, getInboxMapper);
    }

    @ResoInboxScope
    @Provides
    GetInboxBuyerLoadMoreUseCase provideGetInboxBuyerLoadMoreUseCase(
            ResolutionApi resolutionApi, GetInboxMapper getInboxMapper) {
        return new GetInboxBuyerLoadMoreUseCase(resolutionApi, getInboxMapper);
    }

    @ResoInboxScope
    @Provides
    GetInboxSellerLoadMoreUseCase provideGetInboxSellerLoadMoreUseCase(
            ResolutionApi resolutionApi, GetInboxMapper getInboxMapper) {
        return new GetInboxSellerLoadMoreUseCase(resolutionApi, getInboxMapper);
    }

    @ResoInboxScope
    @Provides
    GetInboxBuyerSingleItemUseCase provideGetInboxBuyerSingleItemUseCase(
            ResolutionApi resolutionApi, GetInboxSingleItemMapper getInboxMapper) {
        return new GetInboxBuyerSingleItemUseCase(resolutionApi, getInboxMapper);
    }

    @ResoInboxScope
    @Provides
    GetInboxSellerSingleItemUseCase provideGetInboxSellerSingleItemUseCase(
            ResolutionApi resolutionApi, GetInboxSingleItemMapper getInboxMapper) {
        return new GetInboxSellerSingleItemUseCase(resolutionApi, getInboxMapper);
    }
}
