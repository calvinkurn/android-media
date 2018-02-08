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
    private static ReactNativeHostFactory instance;

    protected ReactNativeHostFactory() {}

    public static ReactNativeHost init(Application application) {
        if(instance == null) instance = new ReactNativeHostFactory();

        return instance.createReactNativeHostDev(application);
    }

    private ReactNativeHost createReactNativeHost(final Application application) {
        return new ReactNativeHost(application) {
            @Override
            protected String getJSBundleFile() {
                return CodePush.getJSBundleFile();
            }

            @Override
            public boolean getUseDeveloperSupport() {
                return GlobalConfig.DEBUG;
            }

            @Override
            protected List<ReactPackage> getPackages() {
                return getListPackages(application);
            }
        };
    }

    private ReactNativeHost createReactNativeHostDev(final Application application) {
        return new ReactNativeHost(application) {
            @Override
            public boolean getUseDeveloperSupport() {
                return true;
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

    protected List<ReactPackage> getListPackages(Application application) {
        return Arrays.<ReactPackage>asList(
                new MainReactPackage(),
                new CoreReactPackage(),
                new CodePush(getCodePushDeploymentKey(), application, GlobalConfig.isAllowDebuggingTools())
        );
    }

    @NonNull
    private static String getCodePushDeploymentKey() {
        if (GlobalConfig.isAllowDebuggingTools()) {
            return ReactConst.CODE_PUSH_DEPLOYMENT_KEY_STAGING;
        }else {
            return ReactConst.CODE_PUSH_DEPLOYMENT_KEY;
        }
    }
}
