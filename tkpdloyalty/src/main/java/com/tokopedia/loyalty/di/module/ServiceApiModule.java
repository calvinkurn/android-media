package com.tokopedia.loyalty.di.module;

import com.tokopedia.core.network.apiservices.transaction.TXService;
import com.tokopedia.core.network.apiservices.transaction.TXVoucherService;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.domain.apiservice.TokoPointService;
import com.tokopedia.loyalty.domain.repository.ITokoPointDBService;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.domain.repository.ITokoPointResponseMapper;
import com.tokopedia.loyalty.domain.repository.TokoPointRepository;
import com.tokopedia.loyalty.domain.repository.TokoPointResponseMapper;

import dagger.Module;
import dagger.Provides;

/**
 * @author anggaprasetiyo on 27/11/17.
 */
@Module(includes = CacheDBModule.class)
public class ServiceApiModule {

    @Provides
    @LoyaltyScope
    TokoPointService provideTokoPointService() {
        return new TokoPointService();
    }

    @Provides
    @LoyaltyScope
    TXVoucherService provideVoucherService() {
        return new TXVoucherService();
    }

    @Provides
    @LoyaltyScope
    TXService provideTxService() {
        return new TXService();
    }

    @Provides
    @LoyaltyScope
    ITokoPointResponseMapper provideITokoPointResponseMapper() {
        return new TokoPointResponseMapper();
    }

    @Provides
    @LoyaltyScope
    ITokoPointRepository provideITokoPointRepository(TokoPointService tokoPointService,
                                                     ITokoPointDBService tokoPointDBService,
                                                     TokoPointResponseMapper tokoPointResponseMapper,
                                                     TXVoucherService txVoucherService,
                                                     TXService txService
    ) {
        return new TokoPointRepository(tokoPointService, tokoPointDBService, tokoPointResponseMapper);
    }
}
