package com.tokopedia.seller.app;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/21/17.
 */
@Module
public abstract class BaseFragmentModule<V extends BaseDiView> {

    private final V view;

    public BaseFragmentModule(V view) {
        this.view = view;
    }

    @Provides
    public V provideView() {
        return view;
    }

}
