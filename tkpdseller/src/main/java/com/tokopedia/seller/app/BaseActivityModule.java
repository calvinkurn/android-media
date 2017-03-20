package com.tokopedia.seller.app;

import android.app.Activity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by sebastianuskh on 3/20/17.
 */
@Module
public class BaseActivityModule {
    final Activity activity;


    public BaseActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    public Activity getActivity() {
        return activity;
    }

}
