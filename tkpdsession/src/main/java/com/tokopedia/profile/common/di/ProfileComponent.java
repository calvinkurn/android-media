package com.tokopedia.profile.common.di;

import android.content.Context;

import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.qualifier.ApplicationContext;
import com.tokopedia.profile.data.network.ProfileApi;
import com.tokopedia.profile.view.activity.TopProfileActivity;

import dagger.Component;

/**
 * Created by nakama on 27/02/18.
 */

@ProfileScope
@Component(modules = ProfileModule.class, dependencies = BaseAppComponent.class)
public interface ProfileComponent {

    @ApplicationContext
    Context context();

    ProfileApi profileApi();

}
