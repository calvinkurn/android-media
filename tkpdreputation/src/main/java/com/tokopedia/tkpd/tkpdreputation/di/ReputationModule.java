package com.tokopedia.tkpd.tkpdreputation.di;

import com.tokopedia.core.database.manager.GlobalCacheManager;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 8/11/17.
 */

@Module
public class ReputationModule {

    @ReputationScope
    @Provides
    GlobalCacheManager provideGlobalCacheManager() {
        return new GlobalCacheManager();
    }
}
