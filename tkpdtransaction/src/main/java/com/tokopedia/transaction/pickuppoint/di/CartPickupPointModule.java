package com.tokopedia.transaction.pickuppoint.di;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.transaction.TXCartActService;
import com.tokopedia.transaction.pickuppoint.data.datastore.CartPickupPointDataStore;
import com.tokopedia.transaction.pickuppoint.data.repository.CartPickupPointRepository;
import com.tokopedia.transaction.pickuppoint.domain.usecase.EditCartPickupPointsUseCase;
import com.tokopedia.transaction.pickuppoint.domain.usecase.RemoveCartPickupPointsUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Irfan Khoirul on 05/01/18.
 */

@Module
public class CartPickupPointModule {

    @Provides
    @CartPickupPointScope
    TXCartActService provideTXCartActService() {
        return new TXCartActService();
    }

    @Provides
    @CartPickupPointScope
    CartPickupPointDataStore provideCartPickupPointDataStore(TXCartActService txCartActService) {
        return new CartPickupPointDataStore(txCartActService);
    }

    @Provides
    @CartPickupPointScope
    CartPickupPointRepository provideCartPickupPointRepository(
            CartPickupPointDataStore cartPickupPointDataStore
    ) {
        return new CartPickupPointRepository(cartPickupPointDataStore);
    }

    @Provides
    @CartPickupPointScope
    EditCartPickupPointsUseCase provideEditCartPickupPointsUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CartPickupPointRepository cartPickupPointRepository
    ) {
        return new EditCartPickupPointsUseCase(threadExecutor, postExecutionThread, cartPickupPointRepository);
    }

    @Provides
    @CartPickupPointScope
    RemoveCartPickupPointsUseCase provideRemoveCartPickupPointsUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            CartPickupPointRepository cartPickupPointRepository
    ) {
        return new RemoveCartPickupPointsUseCase(threadExecutor, postExecutionThread, cartPickupPointRepository);
    }

}
