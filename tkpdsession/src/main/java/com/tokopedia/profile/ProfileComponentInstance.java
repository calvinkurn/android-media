package com.tokopedia.profile;

import android.app.Application;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.profile.common.di.DaggerProfileComponent;
import com.tokopedia.profile.common.di.ProfileComponent;

/**
 * @author by alvinatin on 27/02/18.
 */

public class ProfileComponentInstance {
    private static ProfileComponent profileComponent;

    public static ProfileComponent getProfileComponent(Application application) {
        if (profileComponent == null) {
            profileComponent = DaggerProfileComponent.builder()
                    .baseAppComponent(((BaseMainApplication) application).getBaseAppComponent())
                    .build();
        }
        return profileComponent;
    }
}
