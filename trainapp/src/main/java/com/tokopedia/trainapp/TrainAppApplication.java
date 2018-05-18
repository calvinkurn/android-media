package com.tokopedia.trainapp;

import android.app.Activity;

import com.facebook.stetho.Stetho;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.data.model.analytic.AnalyticTracker;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
import com.tokopedia.train.common.util.TrainDatabase;

import java.io.IOException;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by alvarisi on 3/8/18.
 */

public class TrainAppApplication extends BaseMainApplication implements AbstractionRouter {
    public TrainAppApplication() {
    }

    @Override
    public void onCreate() {
        TrainDatabase.init(this);
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

    @Override
    public void onForceLogout(Activity activity) {

    }

    @Override
    public void showTimezoneErrorSnackbar() {

    }

    @Override
    public void showMaintenancePage() {

    }

    @Override
    public void showForceLogoutDialog(Response response) {

    }

    @Override
    public void showServerError(Response response) {

    }

    @Override
    public void gcmUpdate() throws IOException {

    }

    @Override
    public void refreshToken() throws IOException {

    }

    @Override
    public UserSession getSession() {
        return new UserSession() {
            @Override
            public String getAccessToken() {
                return "xxx";
            }

            @Override
            public String getFreshToken() {
                return "xxx";
            }

            @Override
            public String getUserId() {
                return "xxx";
            }

            @Override
            public String getDeviceId() {
                return "xxx";
            }

            @Override
            public boolean isLoggedIn() {
                return false;
            }

            @Override
            public String getShopId() {
                return "xxx";
            }

            @Override
            public boolean hasShop() {
                return false;
            }

            @Override
            public String getName() {
                return "xxx";
            }

            @Override
            public String getProfilePicture() {
                return "xxx";
            }

            @Override
            public boolean isMsisdnVerified() {
                return false;
            }

            @Override
            public boolean isHasPassword() {
                return false;
            }
        };
    }

    @Override
    public void init() {

    }

    @Override
    public void registerShake(String screenName) {

    }

    @Override
    public void unregisterShake() {

    }

    @Override
    public CacheManager getGlobalCacheManager() {
        return new CacheManager() {
            @Override
            public void save(String key, String value, long durationInSeconds) {

            }

            @Override
            public void delete(String key) {

            }

            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public boolean isExpired(String key) {
                return false;
            }
        };
    }

    @Override
    public AnalyticTracker getAnalyticTracker() {
        return new AnalyticTracker() {
            @Override
            public void sendEventTracking(Map<String, Object> events) {

            }

            @Override
            public void sendEventTracking(String event, String category, String action, String label) {

            }

            @Override
            public void sendScreen(Activity activity, String screenName) {

            }

            @Override
            public void sendEnhancedEcommerce(Map<String, Object> trackingData) {

            }
        };
    }

    @Override
    public void showForceHockeyAppDialog() {

    }

    @Override
    public void logInvalidGrant(Response response) {

    }
}
