package com.tokopedia.tkpdstream.common.di.module;


import android.content.Context;

import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.tkpdstream.common.di.scope.StreamScope;

import dagger.Module;
import dagger.Provides;

/**
 * @author by nisie on 2/1/18.
 */

@StreamScope
@Module(includes = {StreamNetModule.class})
public class StreamModule {

    @StreamScope
    @Provides
    public AnalyticTracker provideAnalyticTracker(@ApplicationContext Context context) {
        if (context instanceof AbstractionRouter) {
            return ((AbstractionRouter) context).getAnalyticTracker();
        }
        throw new RuntimeException("App should implement " + AbstractionRouter.class.getSimpleName());
    }

}
