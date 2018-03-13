package com.tokopedia.tkpdtrain.station.di;

import com.tokopedia.tkpdtrain.common.domain.TrainRepository;
import com.tokopedia.tkpdtrain.station.domain.TrainGetPopularStationsUseCase;
import com.tokopedia.tkpdtrain.station.domain.TrainGetStationCitiesByKeywordUseCase;
import com.tokopedia.tkpdtrain.station.domain.TrainGetStationsByKeywordUseCase;

import dagger.Module;
import dagger.Provides;

/**
 * @author  by alvarisi on 3/5/18.
 */
@Module
public class TrainStationModule {
    @Provides
    TrainGetPopularStationsUseCase provideGetPopularStationsUseCase(TrainRepository trainRepository) {
        return new TrainGetPopularStationsUseCase(trainRepository);
    }

    @Provides
    TrainGetStationsByKeywordUseCase provideGetStationsByKeywordUseCase(TrainRepository trainRepository) {
        return new TrainGetStationsByKeywordUseCase(trainRepository);
    }

    @Provides
    TrainGetStationCitiesByKeywordUseCase provideTrainGetStationCitiesByKeywordUseCase(TrainRepository trainRepository) {
        return new TrainGetStationCitiesByKeywordUseCase(trainRepository);
    }
}
