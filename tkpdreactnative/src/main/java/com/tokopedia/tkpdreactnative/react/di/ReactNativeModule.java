package com.tokopedia.tkpdreactnative.react.di;

import android.app.Application;

import com.facebook.react.ReactNativeHost;
import com.tokopedia.tkpdreactnative.react.ReactNativeHostFactory;
import com.tokopedia.tkpdreactnative.react.ReactUtils;

import dagger.Module;
import dagger.Provides;

/**
 * @author by alvarisi on 9/14/17.
 */

@Module
public class ReactNativeModule {
    private Application reactApplication;
    public ReactNativeModule(Application reactApplication) {
        this.reactApplication = reactApplication;
    }

    @Provides
    @ReactNativeScope
    ReactNativeHost provideReactNativeHostFactory() {
        return ReactNativeHostFactory.init(reactApplication);
    }

    @Provides
    @ReactNativeScope
    ReactUtils provideReactUtils(ReactNativeHost reactNativeHost) {
        return ReactUtils.init(reactNativeHost.getReactInstanceManager());
    }
}
