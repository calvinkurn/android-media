package com.tokopedia.profile.di;

import com.tokopedia.profile.common.di.ProfileComponent;
import com.tokopedia.profile.view.fragment.TopProfileFragment;

import dagger.Component;

/**
 * Created by nakama on 28/02/18.
 */

@ProfileContentScope
@Component(modules = ProfileContentModule.class, dependencies = ProfileComponent.class)
public interface ProfileContentComponent {
    void inject(TopProfileFragment topProfileFragment);
}
