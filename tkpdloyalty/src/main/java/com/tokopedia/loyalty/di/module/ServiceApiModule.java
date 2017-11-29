package com.tokopedia.loyalty.di.module;

import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.domain.apiservice.TokoplusService;
import com.tokopedia.loyalty.domain.repository.ITokoplusRepository;
import com.tokopedia.loyalty.domain.repository.ITokoplusResponseMapper;
import com.tokopedia.loyalty.domain.repository.TokoplusRepository;
import com.tokopedia.loyalty.domain.repository.TokoplusResponseMapper;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 27/11/17.
 */
@Module
public class ServiceApiModule {

    @Provides
    @LoyaltyScope
    TokoplusService provideTokoplusService() {
        return new TokoplusService();
    }

    @Provides
    @LoyaltyScope
    ITokoplusResponseMapper provideITokoplusResponseMapper() {
        return new TokoplusResponseMapper();
    }

    @Provides
    @LoyaltyScope
    ITokoplusRepository provideITokoplusRepository(TokoplusService loyaltyService,
                                                   TokoplusResponseMapper tokoplusResponseMapper) {
        return new TokoplusRepository(loyaltyService, tokoplusResponseMapper);
    }
}
