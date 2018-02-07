package com.tokopedia.loyalty.di.module;

import com.tokopedia.core.network.apiservices.transaction.TXVoucherService;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.domain.apiservice.DigitalEndpointService;
import com.tokopedia.loyalty.domain.apiservice.PromoEndpointService;
import com.tokopedia.loyalty.domain.apiservice.TokoPointService;
import com.tokopedia.loyalty.domain.repository.IPromoRepository;
import com.tokopedia.loyalty.domain.repository.IPromoResponseMapper;
import com.tokopedia.loyalty.domain.repository.ITokoPointDBService;
import com.tokopedia.loyalty.domain.repository.ITokoPointRepository;
import com.tokopedia.loyalty.domain.repository.ITokoPointResponseMapper;
import com.tokopedia.loyalty.domain.repository.PromoRepository;
import com.tokopedia.loyalty.domain.repository.PromoResponseMapper;
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
    DigitalEndpointService provideDigitalEndpointService() {
        return new DigitalEndpointService();
    }

    @Provides
    PromoEndpointService providePromoEndpointService() {
        return new PromoEndpointService();
    }

    @Provides
    @LoyaltyScope
    ITokoPointResponseMapper provideITokoPointResponseMapper() {
        return new TokoPointResponseMapper();
    }

    @Provides
    IPromoResponseMapper provideIPromoResponseMapper() {
        return new PromoResponseMapper();
    }

    @Provides
    @LoyaltyScope
    ITokoPointRepository provideITokoPointRepository(TokoPointService tokoPointService,
                                                     ITokoPointDBService tokoPointDBService,
                                                     TokoPointResponseMapper tokoPointResponseMapper,
                                                     TXVoucherService txVoucherService,
                                                     DigitalEndpointService digitalEndpointService
    ) {
        return new TokoPointRepository(
                tokoPointService,
                tokoPointDBService,
                tokoPointResponseMapper,
                txVoucherService,
                digitalEndpointService);
    }

    @Provides
    IPromoRepository provideIPromoRepository(PromoEndpointService promoEndpointService,
                                             IPromoResponseMapper iPromoResponseMapper) {
        return new PromoRepository(promoEndpointService, iPromoResponseMapper);
    }
}
