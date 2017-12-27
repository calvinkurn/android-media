package com.tokopedia.transaction.pickup.alfamart.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.kero.KeroAuthService;
import com.tokopedia.transaction.pickup.alfamart.data.datastore.PickupPointDataStore;
import com.tokopedia.transaction.pickup.alfamart.domain.mapper.PickupPointEntityMapper;
import com.tokopedia.transaction.pickup.alfamart.data.repository.PickupPointRepository;
import com.tokopedia.transaction.pickup.alfamart.domain.usecase.GetPickupPointsUseCase;
import com.tokopedia.transaction.pickup.alfamart.view.PickupPointContract;
import com.tokopedia.transaction.pickup.alfamart.view.PickupPointPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

@Module
public class PickupPointModule {

    private static final int RETRY_COUNT = 0;

    @Provides
    @PickupPointScope
    KeroAuthService provideKeroAuthService() {
        return new KeroAuthService(RETRY_COUNT);
    }

    @Provides
    @PickupPointScope
    PickupPointDataStore providePickupPointDataStore(KeroAuthService keroAuthService) {
        return new PickupPointDataStore(keroAuthService);
    }

    @Provides
    @PickupPointScope
    PickupPointEntityMapper provideEntityMapper() {
        return new PickupPointEntityMapper();
    }

    @Provides
    @PickupPointScope
    PickupPointRepository providePickupPointRepository(
            PickupPointDataStore pickupPointDataStore,
            PickupPointEntityMapper pickupPointEntityMapper
    ) {
        return new PickupPointRepository(pickupPointDataStore,
                pickupPointEntityMapper);
    }

    @Provides
    @PickupPointScope
    GetPickupPointsUseCase provideGetPickupPointsUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            PickupPointRepository pickupPointRepository
    ) {
        return new GetPickupPointsUseCase(threadExecutor, postExecutionThread, pickupPointRepository);
    }

    @Provides
    @PickupPointScope
    PickupPointContract.Presenter providePickupPointPresenter(
            GetPickupPointsUseCase getPickupPointsUseCase) {
        return new PickupPointPresenter(getPickupPointsUseCase);
    }

}
