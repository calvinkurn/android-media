package com.tokopedia.profile.di;

import com.tokopedia.profile.common.di.ProfileComponent;
import com.tokopedia.profile.view.fragment.TopProfileFragment;

import dagger.Component;

/**
 * @author by alvinatin on 28/02/18.
 */

@TopProfileScope
@Component(modules = TopProfileModule.class, dependencies = ProfileComponent.class)
public interface TopProfileComponent {
    void inject(TopProfileFragment topProfileFragment);
}
