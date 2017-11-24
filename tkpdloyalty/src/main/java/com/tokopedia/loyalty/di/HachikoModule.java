package com.tokopedia.loyalty.di;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 24/11/17.
 */

@Module
public class HachikoModule {

    public HachikoModule() {
    }

    @Provides
    @HachikoModuleScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

}
