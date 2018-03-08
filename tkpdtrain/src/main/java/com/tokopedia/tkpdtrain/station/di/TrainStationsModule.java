package com.tokopedia.tkpdtrain.station.di;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.station.domain.GetPopularStationsUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * Created by alvarisi on 3/5/18.
 */
@Module
public class TrainStationsModule {
    @Provides
    GetPopularStationsUseCase provideGetPopularStationsUseCase(TrainRepository trainRepository) {
        return new GetPopularStationsUseCase(trainRepository);
    }
}
