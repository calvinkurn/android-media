package com.tokopedia.tkpdreactnative.react;

import android.app.Application;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.facebook.react.shell.MainReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.tokopedia.core.util.GlobalConfig;

import java.util.Arrays;
import java.util.List;

/**
 * @author ricoharisin .
 */

public class ReactNativeHostFactory {


    public static ReactNativeHost init(Application application) {
        /*reactInstanceManager = ReactInstanceManager.builder()
                .setApplication(application)
                .setBundleAssetName("index.android.bundle")
                .setJSMainModuleName("reactscript/index.android")
                .addPackage(new MainReactPackage())
                .addPackage(new CoreReactPackage())
                .setUseDeveloperSupport(true)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build();*/

        return createReactNativeHost(application);

    }

    private static ReactNativeHost createReactNativeHost(final Application application) {
        return new ReactNativeHost(application) {
            @Override
            protected String getJSBundleFile() {
                return CodePush.getJSBundleFile();
            }

            @Override
            public boolean getUseDeveloperSupport() {
                return false;
            }

            @Override
            protected List<ReactPackage> getPackages() {
                return getListPackages(application);
            }
        };
    }

    private static ReactNativeHost createReactNativeHostDev(final Application application) {
        return new ReactNativeHost(application) {
            @Override
            public boolean getUseDeveloperSupport() {
                return false;
            }

            @Override
            protected List<ReactPackage> getPackages() {
                return getListPackages(application);
            }

            @Override
            protected String getJSBundleFile() {
                return "index.android.bundle";
            }

            @Override
            protected String getJSMainModuleName() {
                return "index.android";
            }
        };
    }

    private static List<ReactPackage> getListPackages(Application application) {
        return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new CoreReactPackage(),
                new CodePush(getCodePushDeploymentKey(), application, false)
        );
    }

    @NonNull
    private static String getCodePushDeploymentKey() {
        if (false) {
            return ReactConst.CODE_PUSH_DEPLOYMENT_KEY_STAGING;
        }else {
            return ReactConst.CODE_PUSH_DEPLOYMENT_KEY;
        }
    }
}
