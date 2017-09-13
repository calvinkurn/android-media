package com.tokopedia.tkpdreactnative.react;

import android.support.annotation.Nullable;

import com.facebook.react.ReactApplication;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;

/**
 * @author by alvarisi on 8/11/17.
 */

public class ReactUtils {
    private static
    @javax.annotation.Nullable
    ReactUtils reactUtils;
    private ReactApplication reactApplication;

    private ReactUtils(ReactApplication application) {
        this.reactApplication = application;
    }

    private ReactContext getCurrentReactContext() {
        if (reactApplication != null) {
            return reactApplication.getReactNativeHost().getReactInstanceManager().getCurrentReactContext();
        }
        return null;
    }

    public void sendAddWishlistEmitter(String productId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("product_id", productId);
        params.putString("user_id", userId);
        sendEmittEvent(
                getCurrentReactContext(),
                ReactConst.EventEmitter.WISHLIST_ADD,
                params
        );
    }

    public void sendRemoveWishlistEmitter(String productId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("product_id", productId);
        params.putString("user_id", userId);
        sendEmittEvent(
                getCurrentReactContext(),
                ReactConst.EventEmitter.WISHLIST_REMOVE,
                params
        );
    }

    public void sendAddFavoriteEmitter(String shopId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("shop_id", shopId);
        params.putString("user_id", userId);
        sendEmittEvent(
                getCurrentReactContext(),
                ReactConst.EventEmitter.FAVORITE_ADD,
                params
        );
    }

    public void sendRemoveFavoriteEmitter(String shopId, String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("shop_id", shopId);
        params.putString("user_id", userId);
        sendEmittEvent(
                getCurrentReactContext(),
                ReactConst.EventEmitter.FAVORITE_REMOVE,
                params
        );
    }

    public void sendLoginEmitter(String userId) {
        WritableMap params = Arguments.createMap();
        params.putString("user_id", userId);
        sendEmittEvent(
                getCurrentReactContext(),
                ReactConst.EventEmitter.LOGIN,
                params
        );
    }

    private void sendEmittEvent(ReactContext reactContext,
                                String eventName,
                                @Nullable WritableMap params) {
        if (reactContext != null) {
            reactContext
                    .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                    .emit(eventName, params);
        }
    }

    public void sendDestroyPageEmitter() {
        WritableMap params = Arguments.createMap();
        sendEmittEvent(
                getCurrentReactContext(),
                ReactConst.EventEmitter.PAGE_DESTROYED,
                params
        );
    }

    public static ReactUtils init(ReactApplication application) {
        if (reactUtils == null)
            reactUtils = new ReactUtils(application);
        return reactUtils;
    }
}
