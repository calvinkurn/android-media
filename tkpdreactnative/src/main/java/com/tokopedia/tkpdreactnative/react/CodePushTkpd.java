package com.tokopedia.tkpdreactnative.react;

import android.content.Context;

import com.facebook.react.bridge.JavaScriptModule;
import com.microsoft.codepush.react.CodePush;

import java.util.Collections;
import java.util.List;

/**
 * @author ricoharisin .
 */

public class CodePushTkpd extends CodePush {
    public CodePushTkpd(String deploymentKey, Context context) {
        super(deploymentKey, context);
    }

    public CodePushTkpd(String deploymentKey, Context context, boolean isDebugMode) {
        super(deploymentKey, context, isDebugMode);
    }

    public CodePushTkpd(String deploymentKey, Context context, boolean isDebugMode, String serverUrl) {
        super(deploymentKey, context, isDebugMode, serverUrl);
    }

    @Override
    public List<Class<? extends JavaScriptModule>> createJSModules() {
        return Collections.emptyList();
    }
}
